package io.github.zero88.iot.connector.kafka.handler.producer;

import io.github.zero88.iot.connector.kafka.service.KafkaProducerService;
import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.event.EventAction;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.kafka.client.producer.RecordMetadata;

import lombok.NonNull;

/**
 * Kafka Producer Handler is represented for
 * <ul>
 * <li>{@code Producer Record} transformer before sending record to {@code Kafka cluster}</li>
 * <li>{@code Completion handler} after sending record to {@code Kafka cluster}</li>
 * </ul>
 *
 * @see KafkaProducerRecordTransformer
 * @see RecordMetadata
 */
public interface KafkaProducerHandler<T extends KafkaProducerRecordTransformer>
    extends Handler<AsyncResult<RecordMetadata>>, HasSharedData {

    static KafkaProducerHandler logHandler(@NonNull SharedDataLocalProxy sharedData) {
        return new LogKafkaProducerHandler(sharedData);
    }

    void handleSuccess(RecordMetadata metadata);

    void handleFailed(ErrorMessage message);

    /**
     * Translates value and includes {@code EventAction} before sending to {@code Kafka cluster}
     *
     * @return transformer
     * @see KafkaProducerService#publish(EventAction, String, Object)
     * @see EventAction
     */
    T transformer();

    /**
     * System will register it automatically. You don't need call it directly
     *
     * @param transformer Given transformer
     * @return a reference to this, so the API can be used fluently
     */
    KafkaProducerHandler register(T transformer);

}
