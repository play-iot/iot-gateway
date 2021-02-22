package io.github.zero88.iot.connector.kafka.handler.consumer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsible to serve many {@code Kafka topic} with same pair of {@code ConsumerRecord key} and {@code ConsumerRecord
 * value}
 *
 * @param <K> Type of {@code ConsumerRecord key}
 * @param <V> Type of {@code ConsumerRecord value}
 */
@Slf4j
@Builder(builderClassName = "Builder")
public final class ConsumerDispatcher<K, V> implements Consumer<KafkaConsumerRecord<K, V>> {

    @NonNull
    @Singular("handler")
    private final Map<String, KafkaConsumerHandler> handlersByTopic;

    public Set<String> topics() {
        return handlersByTopic.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(KafkaConsumerRecord<K, V> record) {
        KafkaConsumerHandler consumerHandler = handlersByTopic.get(record.topic());
        if (Objects.isNull(consumerHandler)) {
            log.warn("No handler for topic {}", record.topic());
            return;
        }
        consumerHandler.accept(record);
    }

}
