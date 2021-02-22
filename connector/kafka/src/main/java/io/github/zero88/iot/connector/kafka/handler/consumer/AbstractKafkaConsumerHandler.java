package io.github.zero88.iot.connector.kafka.handler.consumer;

import java.util.Objects;

import io.github.zero88.qwe.component.SharedDataLocalProxy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @see KafkaConsumerHandler
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractKafkaConsumerHandler<K, V, T extends KafkaConsumerRecordTransformer<K, V, R>, R>
    implements KafkaConsumerHandler<K, V, T, R> {

    @Getter
    @Accessors(fluent = true)
    private final SharedDataLocalProxy sharedData;
    private T transformer;

    @Override
    public final KafkaConsumerHandler register(T transformer) {
        if (Objects.nonNull(transformer)) {
            this.transformer = transformer;
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final T transformer() {
        return Objects.isNull(this.transformer) ? (T) KafkaConsumerRecordTransformer.DEFAULT : this.transformer;
    }

}
