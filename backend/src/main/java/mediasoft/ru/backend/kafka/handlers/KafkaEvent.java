package mediasoft.ru.backend.kafka.handlers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.models.dto.kafka.CreateOrderKafkaEventDTO;
import mediasoft.ru.backend.models.dto.kafka.DeleteOrderKafkaEventDTO;
import mediasoft.ru.backend.models.dto.kafka.UpdateOrderKafkaEventDTO;
import mediasoft.ru.backend.models.dto.kafka.UpdateOrderStatusKafkaEventDTO;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "event")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOrderKafkaEventDTO.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderKafkaEventDTO.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderStatusKafkaEventDTO.class, name = "DELETE_ORDER"),
        @JsonSubTypes.Type(value = DeleteOrderKafkaEventDTO.class, name = "UPDATE_ORDER_STATUS")
})
public interface KafkaEvent {
    KafkaEventSource getEvent();
}
