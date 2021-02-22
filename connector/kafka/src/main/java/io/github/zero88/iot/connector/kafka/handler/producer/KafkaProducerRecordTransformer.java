package io.github.zero88.iot.connector.kafka.handler.producer;

import java.util.Map;

import io.github.zero88.qwe.event.EventMessage;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

/**
 * Represents to translate {@code raw value} to {@code KafkaProducerRecord}
 *
 * @param <K> Type of {@code ProducerRecord} key
 * @param <V> Type of {@code ProducerRecord} value
 */
public interface KafkaProducerRecordTransformer<K, V> {

    KafkaProducerRecordTransformer DEFAULT = new ProducerRecordTransformer();

    KafkaProducerRecord<K, V> apply(EventMessage msg, String topic, Integer partition, K key, V value,
                                    Map<String, Object> headers);

}
