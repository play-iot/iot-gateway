package io.github.zero88.iot.connector.kafka.handler.producer;

import java.util.Map;

import io.github.zero88.iot.connector.kafka.handler.KafkaHeaderConverter;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.utils.DateTimes;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import lombok.RequiredArgsConstructor;

/**
 * Transform from {@code EventMessage} to {@code ProducerRecord}
 *
 * @param <K> Type of {@code ProducerRecord} key
 * @param <V> Type of {@code ProducerRecord} value
 */
@RequiredArgsConstructor
public class ProducerRecordTransformer<K, V> implements KafkaProducerRecordTransformer<K, V> {

    @Override
    public KafkaProducerRecord<K, V> apply(EventMessage message, String topic, Integer partition, K key, V value,
                                           Map<String, Object> headers) {
        KafkaProducerRecord<K, V> record = KafkaProducerRecord.create(topic, key, value, DateTimes.nowMilli(),
                                                                      partition);
        record.addHeaders(KafkaHeaderConverter.convert(headers));
        record.addHeaders(KafkaHeaderConverter.convert(message));
        return record;
    }

}
