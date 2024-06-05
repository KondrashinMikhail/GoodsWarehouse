package mediasoft.ru.backend.services.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.exceptions.AccessDeniedException;
import mediasoft.ru.backend.exceptions.BlockedCustomerException;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.models.dto.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.customer.CustomerInfo;
import mediasoft.ru.backend.models.dto.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.OrderInfo;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;
    private final ProductService productService;
    private final WebClient webClientAccount;
    private final WebClient webClientCrm;

    @Value("${rest.account-service.methods.get-account-number}")
    private String GET_ACCOUNT_NUMBER_METHOD;

    @Value("${rest.crm-service.methods.get-inn}")
    private String GET_INN_METHOD;

    @Override
    @Transactional
    public CreateOrderResponseDTO createOrder(UUID customerId, CreateOrderRequestDTO createOrderRequestDTO) {
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
    public void changeProductsInOrder(UUID customerId, List<ProductInOrderRequestDTO> productsInOrderRequest, UUID orderId) {
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
    public OrderInfoResponseDTO getOrder(UUID customerId, UUID orderId) {
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
    public void deleteOrder(UUID customerId, UUID orderId) {
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
    public UpdateOrderStatusResponseDTO updateOrderStatus(UUID customerId, OrderStatus status, UUID orderId) {
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

    @Override
    @Transactional
    public Map<UUID, List<OrderInfo>> getProductsInOrders() {
        List<Order> suitableOrders = orderRepository.findAllByStatusIn(List.of(OrderStatus.CREATED, OrderStatus.CONFIRMED));
        List<String> logins = suitableOrders.stream().map(order -> order.getCustomer().getLogin()).toList();

        Map<String, String> innMap;
        Map<String, String> accountNumberMap;

        CompletableFuture<Map<String, String>> fetchInn = CompletableFuture.supplyAsync(() -> webClientCrm
                .post()
                .uri(GET_INN_METHOD)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(Map.class)
                .block());

        CompletableFuture<Map<String, String>> fetchAccountNumber = CompletableFuture.supplyAsync(() -> webClientAccount
                .post()
                .uri(GET_ACCOUNT_NUMBER_METHOD)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(Map.class)
                .block());

        List<OrderProduct> orderProducts = suitableOrders.stream().map(Order::getOrderProducts).flatMap(List::stream).toList();

        Map<UUID, List<OrderInfo>> result = new HashMap<>();

        Map<UUID, List<OrderProduct>> orderProductsMap = orderProducts.stream()
                .collect(Collectors.groupingBy(orderProduct -> orderProduct.getProduct().getId()));

        innMap = fetchInn.join();
        accountNumberMap = fetchAccountNumber.join();

        orderProductsMap.forEach((productId, orderProduct) -> result.put(
                productId,
                orderProduct.stream().map(op -> {
                    Order order = op.getOrder();
                    Customer customer = order.getCustomer();
                    String login = customer.getLogin();

                    return OrderInfo.builder()
                            .id(order.getId())
                            .status(order.getStatus())
                            .deliveryAddress(order.getDeliveryAddress())
                            .customer(CustomerInfo.builder()
                                    .id(customer.getId())
                                    .email(customer.getMail())
                                    .inn(innMap.get(login))
                                    .accountNumber(accountNumberMap.get(login))
                                    .build())
                            .quantity(op.getProductCount())
                            .build();

                }).toList())
        );

        return result;
    }

    private void checkCustomerToOrderAccess(UUID customerId, Order order) {
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
