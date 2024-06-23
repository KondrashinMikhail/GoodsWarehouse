package mediasoft.ru.backend.services.order;

import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.web.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.web.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.web.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.web.response.order.OrderInfo;
import mediasoft.ru.backend.web.response.order.OrderInfoResponseDTO;
import mediasoft.ru.backend.web.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.persistence.entities.Order;
import mediasoft.ru.backend.persistence.entities.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderService {
    CreateOrderResponseDTO createOrder(UUID customerId, CreateOrderRequestDTO createOrderRequestDTO);

    void changeProductsInOrder(UUID customerId, List<ProductInOrderRequestDTO> products, UUID orderId);

    OrderInfoResponseDTO getOrder(UUID customerId, UUID orderId);

    void deleteOrder(UUID customerId, UUID orderId);

    String confirmOrder(UUID orderId);

    void setDeliveryDate(LocalDate deliveryDate, UUID orderId);

    UpdateOrderStatusResponseDTO updateOrderStatus(UUID customerId, OrderStatus status, UUID orderId);

    void addProductToOrder(Product product, BigDecimal productCount, Order order);

    Order getEntityById(UUID id);

    Map<UUID, List<OrderInfo>> getProductsInOrders();
}
