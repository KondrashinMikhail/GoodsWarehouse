package mediasoft.ru.backend.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import mediasoft.ru.backend.exceptions.NotImplementedException;
import mediasoft.ru.backend.kafka.handlers.KafkaEvent;
import mediasoft.ru.backend.kafka.handlers.KafkaEventHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true")
public class CustomKafkaListener {
    private Set<KafkaEventHandler<KafkaEvent>> eventHandlers;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);

            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new NotImplementedException("Handler for eventsource not found"))
                    .handleEvent(eventSource);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
