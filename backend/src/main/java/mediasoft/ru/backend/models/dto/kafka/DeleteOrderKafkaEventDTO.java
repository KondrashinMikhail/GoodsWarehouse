package mediasoft.ru.backend.models.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.kafka.handlers.KafkaEvent;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteOrderKafkaEventDTO implements KafkaEvent {
    private UUID orderId;
    private UUID customerId;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public KafkaEventSource getEvent() {
        return KafkaEventSource.DELETE_ORDER;
    }
}
