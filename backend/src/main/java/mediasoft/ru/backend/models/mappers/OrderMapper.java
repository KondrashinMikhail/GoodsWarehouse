package mediasoft.ru.backend.models.mappers;

import mediasoft.ru.backend.models.dto.kafka.CreateOrderKafkaEventDTO;
import mediasoft.ru.backend.models.dto.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.order.CreateOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.order.UpdateOrderStatusResponseDTO;
import mediasoft.ru.backend.models.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    UpdateOrderStatusResponseDTO mapModelToUpdateOrderStatusDTO(Order order);

    CreateOrderResponseDTO mapModelToCreateOrderResponseDTO(Order order);

    CreateOrderRequestDTO mapKafkaEventToCreateOrderRequest(CreateOrderKafkaEventDTO kafkaEvent);
}
