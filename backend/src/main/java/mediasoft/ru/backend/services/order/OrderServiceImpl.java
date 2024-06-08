package mediasoft.ru.backend.services.order;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.exceptions.AccessDeniedException;
import mediasoft.ru.backend.exceptions.BlockedCustomerException;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.models.dto.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.OrderInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.models.entities.Customer;
import mediasoft.ru.backend.models.entities.Order;
import mediasoft.ru.backend.models.entities.OrderProduct;
import mediasoft.ru.backend.models.entities.Product;
import mediasoft.ru.backend.models.mappers.OrderMapper;
import mediasoft.ru.backend.repositories.OrderProductRepository;
import mediasoft.ru.backend.repositories.OrderRepository;
import mediasoft.ru.backend.services.customer.CustomerService;
import mediasoft.ru.backend.services.product.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderProductRepository orderProductRepository;
    private OrderMapper orderMapper;
    private CustomerService customerService;
    private ProductService productService;

    @Override
    @Transactional
    public CreateOrderResponseDTO createOrder(Long customerId, CreateOrderRequestDTO createOrderRequestDTO) {
        String deliveryAddress = createOrderRequestDTO.getDeliveryAddress();
        Customer customer = customerService.getEntityById(customerId);
        if (!customer.getIsActive())
            throw new BlockedCustomerException(customer.getId());
        List<ProductInOrderRequestDTO> products = createOrderRequestDTO.getProducts();

        Map<UUID, Product> productsMap = productService.getMapOfProducts(products.stream().map(ProductInOrderRequestDTO::getId).toList());
        productService.checkProductBeforeAddToOrder(products, productsMap);

        Order order = orderRepository.save(Order.builder()
                .customer(customer)
                .deliveryAddress(deliveryAddress)
                .build());
        log.info("Created order with id - {}", order.getId());

        products.forEach(productInOrderDTO -> {
            Product product = productsMap.get(productInOrderDTO.getId());
            addProductToOrder(product, productInOrderDTO.getCount(), order);
        });

        return orderMapper.mapModelToCreateOrderResponseDTO(order);
    }

    @Override
    @Transactional
    public void changeProductsInOrder(Long customerId, List<ProductInOrderRequestDTO> productsInOrderRequest, UUID orderId) {
        Order order = getEntityById(orderId);
        checkOrderStatusAccess(order, OrderStatus.CREATED);
        checkCustomerToOrderAccess(customerId, order);
        Map<UUID, Product> productsMap = productService.getMapOfProducts(productsInOrderRequest.stream().map(ProductInOrderRequestDTO::getId).toList());
        productService.checkProductBeforeAddToOrder(productsInOrderRequest, productsMap);

        productsInOrderRequest.forEach(product -> {
            Product productEntity = productsMap.get(product.getId());

            Optional<OrderProduct> orderProduct = order.getOrderProducts().stream()
                    .filter(orderProducts -> orderProducts.getProduct().getId() == productEntity.getId())
                    .findFirst();

            if (orderProduct.isEmpty())
                addProductToOrder(productEntity, product.getCount(), order);
            else {
                OrderProduct orderProductExtracted = orderProduct.get();
                orderProductExtracted.setProductCount(orderProductExtracted.getProductCount().add(product.getCount()));
                orderProductExtracted.setProductPrice(productEntity.getPrice());
                orderProductExtracted.setLastModifiedDate(LocalDateTime.now());
                orderProductRepository.save(orderProductExtracted);
                log.info("Updated product with id - {} in order with id - {}", product.getId(), order.getId());
                productService.decrementProductCount(productsMap.get(product.getId()), product.getCount());
            }
        });
    }

    @Override
    public OrderInfoResponseDTO getOrder(Long customerId, UUID orderId) {
        Order order = getEntityById(orderId);
        checkCustomerToOrderAccess(customerId, order);

        List<ProductInOrderResponseDTO> products = productService.getProductsInOrder(orderId);

        return OrderInfoResponseDTO.builder()
                .orderId(order.getId())
                .products(products)
                .totalPrice(products.stream()
                        .map(product -> product.getPrice().multiply(product.getQuantity()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Override
    @Transactional
    public void deleteOrder(Long customerId, UUID orderId) {
        Order order = getEntityById(orderId);
        checkOrderStatusAccess(order, OrderStatus.CREATED);
        checkCustomerToOrderAccess(customerId, order);
        order.getOrderProducts()
                .forEach(orderProduct ->
                        productService.returnProduct(orderProduct.getProduct(), orderProduct.getProductCount()));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public void confirmOrder(UUID orderId) {
        //TODO: сделать реализацию позже
    }

    @Override
    public UpdateOrderStatusResponseDTO updateOrderStatus(Long customerId, OrderStatus status, UUID orderId) {
        Order order = getEntityById(orderId);
        checkCustomerToOrderAccess(customerId, order);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order with id - {}, set status - {}", updatedOrder.getId(), updatedOrder.getStatus());
        return orderMapper.mapModelToUpdateOrderStatusDTO(updatedOrder);
    }

    @Override
    public Order getEntityById(UUID id) {
        return orderRepository.findByIdFetchProducts(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Order with id - %s not found!", id)));
    }

    @Override
    public void addProductToOrder(Product product, BigDecimal productCount, Order order) {
        orderProductRepository.save(OrderProduct.builder()
                .product(product)
                .order(order)
                .productCount(productCount)
                .productPrice(product.getPrice())
                .lastModifiedDate(LocalDateTime.now())
                .build());
        log.info("Added product with id - {} to order with id - {}", product.getId(), order.getId());
        productService.decrementProductCount(product, productCount);
    }

    private void checkCustomerToOrderAccess(Long customerId, Order order) {
        if (!Objects.equals(order.getCustomer().getId(), customerId))
            throw new AccessDeniedException(String.format("Customer with id - %s can not perform actions on the order %s", customerId, order.getId()));
        Customer customer = order.getCustomer();
        if (!customer.getIsActive())
            throw new BlockedCustomerException(customer.getId());
    }

    private void checkOrderStatusAccess(Order order, OrderStatus status) {
        if (order.getStatus() != status)
            throw new AccessDeniedException(
                    String.format("Can not perform actions on the order %s because it is not in status %s", order.getId(), status.toString()));
    }
}
