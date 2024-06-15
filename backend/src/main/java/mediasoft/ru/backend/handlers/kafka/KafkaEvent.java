package mediasoft.ru.backend.handlers.kafka;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.models.events.CreateOrderKafkaEventDTO;
import mediasoft.ru.backend.models.events.DeleteOrderKafkaEventDTO;
import mediasoft.ru.backend.models.events.UpdateOrderKafkaEventDTO;
import mediasoft.ru.backend.models.events.UpdateOrderStatusKafkaEventDTO;

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
