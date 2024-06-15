package mediasoft.ru.backend.mappers;

import mediasoft.ru.backend.models.events.CreateOrderKafkaEventDTO;
import mediasoft.ru.backend.web.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.web.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.web.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.persistence.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    UpdateOrderStatusResponseDTO mapModelToUpdateOrderStatusDTO(Order order);

    CreateOrderResponseDTO mapModelToCreateOrderResponseDTO(Order order);

    CreateOrderRequestDTO mapKafkaEventToCreateOrderRequest(CreateOrderKafkaEventDTO kafkaEvent);
}
