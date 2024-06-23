package mediasoft.ru.backend.handlers.kafka;

public interface KafkaEventHandler<T extends KafkaEvent> {
    boolean canHandle(KafkaEvent kafkaEvent);

    void handleEvent(T kafkaEvent);
}