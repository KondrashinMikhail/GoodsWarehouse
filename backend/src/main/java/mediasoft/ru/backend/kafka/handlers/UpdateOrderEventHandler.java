package mediasoft.ru.backend.kafka.handlers;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.models.dto.kafka.UpdateOrderKafkaEventDTO;
import mediasoft.ru.backend.services.order.OrderService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateOrderEventHandler implements KafkaEventHandler<UpdateOrderKafkaEventDTO> {
    private OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent kafkaEvent) {
        return kafkaEvent.getEvent().equals(KafkaEventSource.DELETE_ORDER);
    }

    @Override
    public void handleEvent(UpdateOrderKafkaEventDTO kafkaEvent) {
        orderService.changeProductsInOrder(kafkaEvent.getCustomerId(), kafkaEvent.getProducts(), kafkaEvent.getOrderId());
    }
}
