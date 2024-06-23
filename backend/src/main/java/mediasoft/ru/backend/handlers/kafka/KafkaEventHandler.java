package mediasoft.ru.backend.handlers.kafka;

import mediasoft.ru.backend.handlers.kafka.events.KafkaEvent;

public interface KafkaEventHandler<T extends KafkaEvent> {
    boolean canHandle(KafkaEvent kafkaEvent);

    void handleEvent(T kafkaEvent);
}