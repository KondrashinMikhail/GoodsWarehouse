package mediasoft.ru.backend.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.handlers.kafka.KafkaEvent;
import mediasoft.ru.backend.web.request.product.ProductInOrderRequestDTO;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderKafkaEventDTO implements KafkaEvent {
    private UUID customerId;
    private String deliveryAddress;
    private List<ProductInOrderRequestDTO> products;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public KafkaEventSource getEvent() {
        return KafkaEventSource.CREATE_ORDER;
    }
}
