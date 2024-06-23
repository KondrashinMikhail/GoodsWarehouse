package mediasoft.ru.backend.handlers.kafka;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.enums.KafkaEventSource;
import mediasoft.ru.backend.handlers.kafka.events.CreateOrderKafkaEventDTO;
import mediasoft.ru.backend.handlers.kafka.events.KafkaEvent;
import mediasoft.ru.backend.web.request.order.CreateOrderRequestDTO;
import mediasoft.ru.backend.mappers.OrderMapper;
import mediasoft.ru.backend.services.order.OrderService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateOrderEventHandler implements KafkaEventHandler<CreateOrderKafkaEventDTO> {
    private OrderService orderService;
    private OrderMapper orderMapper;

    @Override
    public boolean canHandle(KafkaEvent kafkaEvent) {
        return kafkaEvent.getEvent().equals(KafkaEventSource.CREATE_ORDER);
    }

    @Override
    public void handleEvent(CreateOrderKafkaEventDTO kafkaEvent) {
        CreateOrderRequestDTO request = orderMapper.mapKafkaEventToCreateOrderRequest(kafkaEvent);
        orderService.createOrder(kafkaEvent.getCustomerId(), request);
    }
}
