package mediasoft.ru.backend.kafka.handlers;

public interface KafkaEventHandler<T extends KafkaEvent> {
    boolean canHandle(KafkaEvent kafkaEvent);

    void handleEvent(T kafkaEvent);
}