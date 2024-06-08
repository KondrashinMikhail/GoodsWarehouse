package mediasoft.ru.backend.controllers;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.models.dto.request.order.ChangeOrderStatusRequestDTO;
import mediasoft.ru.backend.models.dto.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.OrderInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.services.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(
            @RequestHeader Long customerId,
            @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        return ResponseEntity.ok(orderService.createOrder(customerId, createOrderRequestDTO));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> addProductToOrder(
            @RequestHeader Long customerId,
            @RequestBody List<ProductInOrderRequestDTO> products,
            @PathVariable UUID orderId) {
        orderService.changeProductsInOrder(customerId, products, orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderInfoResponseDTO> getOrder(
            @RequestHeader Long customerId,
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(customerId, orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(
            @RequestHeader Long customerId,
            @PathVariable UUID orderId) {
        orderService.deleteOrder(customerId, orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/confirm")
    public void confirmOrder(@PathVariable UUID orderId) {
        orderService.confirmOrder(orderId);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<UpdateOrderStatusResponseDTO> updateOrderStatus(
            @RequestHeader Long customerId,
            @RequestBody ChangeOrderStatusRequestDTO changeOrderStatusRequestDTO,
            @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.updateOrderStatus(customerId, changeOrderStatusRequestDTO.getStatus(), orderId));
    }
}
