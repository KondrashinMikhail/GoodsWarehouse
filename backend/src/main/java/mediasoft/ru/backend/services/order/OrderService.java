package mediasoft.ru.backend.services.order;

import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.models.dto.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.OrderInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.models.entities.Order;
import mediasoft.ru.backend.models.entities.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    CreateOrderResponseDTO createOrder(Long customerId, CreateOrderRequestDTO createOrderRequestDTO);

    void changeProductsInOrder(Long customerId, List<ProductInOrderRequestDTO> products, UUID orderId);

    OrderInfoResponseDTO getOrder(Long customerId, UUID orderId);

    void deleteOrder(Long customerId, UUID orderId);

    void confirmOrder(UUID orderId);

    UpdateOrderStatusResponseDTO updateOrderStatus(Long customerId, OrderStatus status, UUID orderId);

    void addProductToOrder(Product product, BigDecimal productCount, Order order);

    Order getEntityById(UUID id);
}
