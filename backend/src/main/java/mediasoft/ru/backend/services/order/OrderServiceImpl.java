package mediasoft.ru.backend.services.order;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.exceptions.AccessDeniedException;
import mediasoft.ru.backend.exceptions.BlockedCustomerException;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.mappers.OrderMapper;
import mediasoft.ru.backend.persistence.entities.Customer;
import mediasoft.ru.backend.persistence.entities.Order;
import mediasoft.ru.backend.persistence.entities.OrderProduct;
import mediasoft.ru.backend.persistence.entities.Product;
import mediasoft.ru.backend.persistence.repositories.OrderProductRepository;
import mediasoft.ru.backend.persistence.repositories.OrderRepository;
import mediasoft.ru.backend.services.account.AccountServiceClient;
import mediasoft.ru.backend.services.crm.CrmServiceClient;
import mediasoft.ru.backend.services.customer.CustomerService;
import mediasoft.ru.backend.services.orchestrator.OrchestratorService;
import mediasoft.ru.backend.services.product.ProductService;
import mediasoft.ru.backend.web.request.order.CamundaProcessOrderRequest;
import mediasoft.ru.backend.web.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.web.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.web.response.customer.CustomerInfo;
import mediasoft.ru.backend.web.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.web.response.order.OrderInfo;
import mediasoft.ru.backend.web.response.order.OrderInfoResponseDTO;
import mediasoft.ru.backend.web.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.web.response.product.ProductInOrderResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    private final AccountServiceClient accountServiceClient;
    private final CrmServiceClient crmServiceClient;
    private final OrchestratorService orchestratorService;

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
    @Transactional
    public String confirmOrder(UUID orderId) {
        Order order = getEntityById(orderId);
        String login = order.getCustomer().getLogin();

        String processId = orchestratorService.startProcess(
                CamundaProcessOrderRequest.builder()
                        .orderId(order.getId())
                        .customerId(order.getCustomer().getId())
                        .accountNumber(accountServiceClient.getAccountNumbers(List.of(login)).join().get(login))
                        .inn(crmServiceClient.getCrms(List.of(login)).join().get(login))
                        .deliveryAddress(order.getDeliveryAddress())
                        .totalPrice(order.getOrderProducts().stream().map(OrderProduct::getProductCount).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build()
        );

        log.info("Created process with id - {}", processId);

        order.setStatus(OrderStatus.PROCESSING);
        order.setProcessId(processId);
        orderRepository.save(order);
        log.info("Updated order with id - {}, set status - {} and process id - {}", order.getId(), order.getStatus(), processId);

        return processId;
    }

    @Override
    public void setDeliveryDate(LocalDate deliveryDate, UUID orderId) {
        Order order = getEntityById(orderId);
        order.setDeliveryDate(deliveryDate);
        Order updatedOrder = orderRepository.save(order);
        log.info("Set delivery date - {} for order with id - {}", deliveryDate, updatedOrder.getId());
    }

    @Override
    public UpdateOrderStatusResponseDTO updateOrderStatus(UUID customerId, OrderStatus status, UUID orderId) {
        Order order = getEntityById(orderId);
        checkCustomerToOrderAccess(customerId, order);
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order with id - {}, new status - {}", updatedOrder.getId(), updatedOrder.getStatus());
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

        List<OrderProduct> orderProducts = suitableOrders.stream().map(Order::getOrderProducts).flatMap(List::stream).toList();

        Map<UUID, List<OrderInfo>> result = new HashMap<>();

        Map<UUID, List<OrderProduct>> orderProductsMap = orderProducts.stream()
                .collect(Collectors.groupingBy(orderProduct -> orderProduct.getProduct().getId()));

        orderProductsMap.forEach((productId, orderProduct) -> result.put(
                productId,
                orderProduct.stream().map(op -> {
                    Order order = op.getOrder();
                    Customer customer = order.getCustomer();
                    String login = customer.getLogin();

                    Map<String, String> accountNumbers = accountServiceClient.getAccountNumbers(logins).join();
                    Map<String, String> inns = crmServiceClient.getCrms(logins).join();

                    return OrderInfo.builder()
                            .id(order.getId())
                            .status(order.getStatus())
                            .deliveryAddress(order.getDeliveryAddress())
                            .customer(CustomerInfo.builder()
                                    .id(customer.getId())
                                    .email(customer.getMail())
                                    .inn(accountNumbers.get(login))
                                    .accountNumber(inns.get(login))
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
