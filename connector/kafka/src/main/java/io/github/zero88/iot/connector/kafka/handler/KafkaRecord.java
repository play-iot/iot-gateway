package io.github.zero88.iot.connector.kafka.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

import io.github.zero88.iot.connector.kafka.handler.RecordMixin.ConsumerRecordMixin;
import io.github.zero88.iot.connector.kafka.handler.RecordMixin.ProducerRecordMixin;
import io.github.zero88.qwe.dto.JsonData;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Kafka record metadata
 *
 * @param <T> Type of {@link ConsumerRecord} or {@link ProducerRecord}
 * @see ConsumerRecord
 * @see ProducerRecord
 * @see JsonData
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaRecord<T> implements JsonData {

    public static final ObjectMapper NO_HEADERS_MAPPER = RecordMixin.MAPPER.copy()
                                                                           .setFilterProvider(
                                                                               RecordMixin.ignoreHeaders());
    @Getter
    @JsonUnwrapped
    private final T record;

    public static <K, V> KafkaRecord<ConsumerRecord<K, V>> serialize(ConsumerRecord<K, V> record) {
        return new KafkaRecord<>(record);
    }

    public static <K, V> KafkaRecord<ProducerRecord<K, V>> serialize(ProducerRecord<K, V> record) {
        return new KafkaRecord<>(record);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> KafkaRecord<ConsumerRecord<K, V>> toConsumer(@NonNull JsonObject data) {
        return new KafkaRecord<>(JsonData.from(data, ConsumerRecordMixin.class, RecordMixin.MAPPER));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> KafkaRecord<ProducerRecord<K, V>> toProducer(@NonNull JsonObject data) {
        return new KafkaRecord<>(JsonData.from(data, ProducerRecordMixin.class, RecordMixin.MAPPER));
    }

    @Override
    public ObjectMapper getMapper() {
        return RecordMixin.MAPPER;
    }

}
