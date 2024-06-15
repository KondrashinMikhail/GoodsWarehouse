package mediasoft.ru.backend.kafka.handlers;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.models.dto.kafka.UpdateOrderStatusKafkaEventDTO;
import mediasoft.ru.backend.services.order.OrderService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateOrderStatusEventHandler implements KafkaEventHandler<UpdateOrderStatusKafkaEventDTO> {
    private OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent kafkaEvent) {
        return kafkaEvent.getEvent().equals(KafkaEventSource.UPDATE_ORDER_STATUS);
    }

    @Override
    public void handleEvent(UpdateOrderStatusKafkaEventDTO kafkaEvent) {
        orderService.updateOrderStatus(kafkaEvent.getCustomerId(), kafkaEvent.getStatus(), kafkaEvent.getOrderId());
    }
}
