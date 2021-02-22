package io.github.zero88.iot.connector.kafka.handler.producer;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.vertx.core.AsyncResult;
import io.vertx.kafka.client.producer.RecordMetadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @see KafkaProducerHandler
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractKafkaProducerHandler<T extends KafkaProducerRecordTransformer>
    implements KafkaProducerHandler<T> {

    @Getter
    @Accessors(fluent = true)
    private final SharedDataLocalProxy sharedData;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private T transformer;

    @Override
    public final void handle(AsyncResult<RecordMetadata> result) {
        if (result.succeeded()) {
            handleSuccess(result.result());
        } else {
            handleFailed(ErrorMessage.parse(result.cause()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T transformer() {
        return Objects.isNull(this.transformer) ? (T) KafkaProducerRecordTransformer.DEFAULT : this.transformer;
    }

    @Override
    public final KafkaProducerHandler register(T transformer) {
        if (Objects.nonNull(transformer)) {
            this.transformer = transformer;
        }
        return this;
    }

}
