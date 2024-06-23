package mediasoft.ru.backend.handlers.kafka.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.enums.OrderStatus;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusKafkaEventDTO implements KafkaEvent {
    private UUID customerId;
    private UUID orderId;
    private OrderStatus status;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public KafkaEventSource getEvent() {
        return KafkaEventSource.UPDATE_ORDER_STATUS;
    }
}
