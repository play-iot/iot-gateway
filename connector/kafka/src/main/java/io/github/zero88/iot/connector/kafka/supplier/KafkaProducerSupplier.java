package io.github.zero88.iot.connector.kafka.supplier;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serializer;

import io.github.zero88.iot.connector.kafka.serialization.QWEKafkaSerdes;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import io.vertx.kafka.client.producer.impl.KafkaProducerImpl;

import lombok.NonNull;

/**
 * Kafka Producer supplier
 *
 * @see QWEKafkaSerdes
 * @see KafkaProducer
 */
public interface KafkaProducerSupplier {

    /**
     * Create a new KafkaProducer instance
     *
     * @param <K>       type of key
     * @param <V>       type of value
     * @param vertx     Vertx instance serialize use
     * @param config    Kafka producer configuration
     * @param keyType   class type for the key serialization
     * @param valueType class type for the value serialization
     * @return an instance of the KafkaProducer
     */
    static <K, V> io.vertx.kafka.client.producer.KafkaProducer<K, V> create(Vertx vertx, @NonNull JsonObject config,
                                                                            Class<K> keyType, Class<V> valueType) {
        return create(vertx, config.getMap(), keyType, valueType);
    }

    /**
     * Create a new KafkaProducer instance
     *
     * @param <K>       type of key
     * @param <V>       type of value
     * @param vertx     Vertx instance serialize use
     * @param config    Kafka producer configuration
     * @param keyType   class type for the key serialization
     * @param valueType class type for the value serialization
     * @return an instance of the KafkaProducer
     */
    static <K, V> io.vertx.kafka.client.producer.KafkaProducer<K, V> create(Vertx vertx, Map<String, Object> config,
                                                                            @NonNull Class<K> keyType,
                                                                            @NonNull Class<V> valueType) {
        Serializer<K> keySerializer = QWEKafkaSerdes.serdeFrom(keyType).serializer();
        Serializer<V> valueSerializer = QWEKafkaSerdes.serdeFrom(valueType).serializer();
        return create(vertx, config, keySerializer, valueSerializer);
    }

    /**
     * Create a new KafkaProducer instance
     *
     * @param <K>             type of key
     * @param <V>             type of value
     * @param vertx           Vertx instance serialize use
     * @param config          Kafka producer configuration
     * @param keySerializer   class type for the key serialization
     * @param valueSerializer class type for the value serialization
     * @return an instance of the KafkaProducer
     */
    static <K, V> io.vertx.kafka.client.producer.KafkaProducer<K, V> create(Vertx vertx, @NonNull JsonObject config,
                                                                            Serializer<K> keySerializer,
                                                                            Serializer<V> valueSerializer) {
        return create(vertx, config.getMap(), keySerializer, valueSerializer);
    }

    /**
     * Create a new KafkaProducer instance
     *
     * @param <K>             type of key
     * @param <V>             type of value
     * @param vertx           Vertx instance serialize use
     * @param config          Kafka producer configuration
     * @param keySerializer   class type for the key serialization
     * @param valueSerializer class type for the value serialization
     * @return an instance of the KafkaProducer
     */
    static <K, V> io.vertx.kafka.client.producer.KafkaProducer<K, V> create(@NonNull Vertx vertx,
                                                                            @NonNull Map<String, Object> config,
                                                                            @NonNull Serializer<K> keySerializer,
                                                                            @NonNull Serializer<V> valueSerializer) {
        return create(vertx, new KafkaProducer<>(config, keySerializer, valueSerializer));
    }

    /**
     * Create a new KafkaProducer instance
     *
     * @param <K>      type of key
     * @param <V>      type of value
     * @param vertx    Vertx instance serialize use
     * @param producer native Kafka producer instance
     * @return an instance of the KafkaProducer
     * @see Producer
     * @see KafkaProducer
     */
    static <K, V> io.vertx.kafka.client.producer.KafkaProducer<K, V> create(@NonNull Vertx vertx,
                                                                            @NonNull Producer<K, V> producer) {
        return new KafkaProducerImpl<>(vertx, KafkaWriteStream.create(vertx, producer)).registerCloseHook();
    }

}
