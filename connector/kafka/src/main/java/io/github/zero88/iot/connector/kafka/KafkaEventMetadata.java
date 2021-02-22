package io.github.zero88.iot.connector.kafka;

import java.util.Objects;

import org.apache.kafka.common.serialization.Serde;

import io.github.zero88.iot.connector.kafka.handler.consumer.KafkaConsumerHandler;
import io.github.zero88.iot.connector.kafka.handler.consumer.KafkaConsumerRecordTransformer;
import io.github.zero88.iot.connector.kafka.handler.producer.KafkaProducerHandler;
import io.github.zero88.iot.connector.kafka.handler.producer.KafkaProducerRecordTransformer;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventModel;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Kafka event metadata for manage {@code KafkaConsumer} and {@code KafkaProducer} in application
 *
 * @param <K> Type of {@code Kafka Record} key
 * @param <V> Type of {@code Kafka Record} value
 * @see EventModel
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(doNotUseGetters = true, onlyExplicitlyIncluded = true)
public final class KafkaEventMetadata<K, V> {

    @NonNull
    @EqualsAndHashCode.Include
    private final String topic;
    @NonNull
    @EqualsAndHashCode.Include
    private final KafkaClientType type;
    @NonNull
    private final ClientTechId<K, V> techId;
    private final EventModel eventModel;
    private final KafkaConsumerHandler consumerHandler;
    private final KafkaConsumerRecordTransformer consumerTransformer;

    private final KafkaProducerHandler producerHandler;
    private final KafkaProducerRecordTransformer producerTransformer;

    private KafkaEventMetadata(String topic, KafkaClientType type, ClientTechId<K, V> techId, EventModel model,
                               KafkaConsumerHandler consumerHandler,
                               KafkaConsumerRecordTransformer consumerTransformer) {
        this(topic, type, techId, model, consumerHandler, consumerTransformer, null, null);
    }

    private KafkaEventMetadata(String topic, KafkaClientType type, ClientTechId<K, V> techId,
                               KafkaProducerHandler producerHandler,
                               KafkaProducerRecordTransformer producerTransformer) {
        this(topic, type, techId, null, null, null, producerHandler, producerTransformer);
    }

    public static <K, V> ConsumerBuilder<K, V> consumer()  { return new ConsumerBuilder<>(); }

    public static <K, V> ProducerBuilder<K, V> producer()  { return new ProducerBuilder<>(); }

    public KafkaConsumerRecordTransformer getTransformer() { return null; }

    public static class ConsumerBuilder<K, V> extends Builder<K, V, ConsumerBuilder> {

        private EventModel eventModel;
        private KafkaConsumerHandler handler;
        private KafkaConsumerRecordTransformer<K, V, EventMessage> transformer;

        ConsumerBuilder() {
            super(KafkaClientType.CONSUMER);
        }

        public ConsumerBuilder<K, V> handler(KafkaConsumerHandler handler) {
            this.handler = handler;
            return this;
        }

        public ConsumerBuilder<K, V> transformer(KafkaConsumerRecordTransformer<K, V, EventMessage> transformer) {
            this.transformer = transformer;
            return this;
        }

        public ConsumerBuilder<K, V> model(@NonNull EventModel model) {
            this.eventModel = model;
            return this;
        }

        @Override
        public KafkaEventMetadata<K, V> build() {
            return new KafkaEventMetadata<>(topic, type, getTechId(),
                                            Objects.requireNonNull(eventModel, "Consumer event cannot be null"),
                                            handler, transformer);
        }

    }


    public static class ProducerBuilder<K, V> extends Builder<K, V, ProducerBuilder> {

        private KafkaProducerHandler handler;
        private KafkaProducerRecordTransformer<K, V> transformer;

        ProducerBuilder() {
            super(KafkaClientType.PRODUCER);
        }

        public ProducerBuilder<K, V> handler(KafkaProducerHandler handler) {
            this.handler = handler;
            return this;
        }

        public ProducerBuilder<K, V> transformer(KafkaProducerRecordTransformer<K, V> transformer) {
            this.transformer = transformer;
            return this;
        }

        @Override
        public KafkaEventMetadata<K, V> build() {
            return new KafkaEventMetadata<>(topic, type, getTechId(), handler, transformer);
        }

    }


    @SuppressWarnings("unchecked")
    public static abstract class Builder<K, V, T extends Builder> {

        protected final @NonNull KafkaClientType type;
        protected String topic;
        private Class<K> keyClass;
        private Class<V> valueClass;
        private Serde<K> keySerdes;
        private Serde<V> valueSerdes;

        private Builder(@NonNull KafkaClientType type) {
            this.type = type;
        }

        public T topic(@NonNull String topic) {
            this.topic = topic;
            return (T) this;
        }

        public T keyClass(Class keyClass) {
            this.keyClass = keyClass;
            return (T) this;
        }

        public T valueClass(Class valueClass) {
            this.valueClass = valueClass;
            return (T) this;
        }

        public T keySerdes(Serde keySerdes) {
            this.keySerdes = keySerdes;
            return (T) this;
        }

        public T valueSerdes(Serde valueSerdes) {
            this.valueSerdes = valueSerdes;
            return (T) this;
        }

        ClientTechId<K, V> getTechId() {
            return new ClientTechId<>(keyClass, keySerdes, valueClass, valueSerdes);
        }

        public abstract KafkaEventMetadata<K, V> build();

    }

}
