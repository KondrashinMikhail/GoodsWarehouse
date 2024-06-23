package mediasoft.ru.backend.handlers.kafka;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.handlers.kafka.events.DeleteOrderKafkaEventDTO;
import mediasoft.ru.backend.handlers.kafka.events.KafkaEvent;
import mediasoft.ru.backend.services.order.OrderService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteOrderEventHandler implements KafkaEventHandler<DeleteOrderKafkaEventDTO> {
    private OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent kafkaEvent) {
        return kafkaEvent.getEvent().equals(KafkaEventSource.DELETE_ORDER);
    }

    @Override
    public void handleEvent(DeleteOrderKafkaEventDTO kafkaEvent) {
        orderService.deleteOrder(kafkaEvent.getCustomerId(), kafkaEvent.getOrderId());
    }
}
