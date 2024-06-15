package mediasoft.ru.backend.configurations.kafka;

import mediasoft.ru.backend.handlers.kafka.KafkaEvent;
import mediasoft.ru.backend.handlers.kafka.KafkaEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class KafkaEventHandlerConfig {
    @Bean
    <T extends KafkaEvent> Set<KafkaEventHandler<T>> eventHandlers(Set<KafkaEventHandler<T>> eventHandlers) {
        return new HashSet<>(eventHandlers);
    }
}
