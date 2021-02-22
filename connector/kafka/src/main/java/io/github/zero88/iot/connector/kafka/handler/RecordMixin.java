package io.github.zero88.iot.connector.kafka.handler;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.record.DefaultRecord;
import org.apache.kafka.common.record.TimestampType;

import io.github.zero88.qwe.dto.JsonData;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

interface RecordMixin extends JsonData {

    ObjectMapper MAPPER = JsonData.MAPPER.copy()
                                         .addMixIn(ConsumerRecord.class, ConsumerRecordMixin.class)
                                         .addMixIn(ProducerRecord.class, ProducerRecordMixin.class)
                                         .addMixIn(Headers.class, HeadersMixin.class)
                                         .addMixIn(Header.class, HeaderMixin.class)
                                         .addMixIn(ByteBuffer.class, ByteBufferIgnoreMixin.class)
                                         .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                                         .setVisibility(PropertyAccessor.SETTER, Visibility.NONE)
                                         .setVisibility(PropertyAccessor.GETTER, Visibility.NONE)
                                         .setVisibility(PropertyAccessor.CREATOR, Visibility.NONE)
                                         .setFilterProvider(ignoreSerializedSize())
                                         .registerModule(new Jdk8Module());

    static FilterProvider createFilterProvider(String... ignoreKeys) {
        return new SimpleFilterProvider().addFilter("kafkaRecordIgnores",
                                                    SimpleBeanPropertyFilter.serializeAllExcept(ignoreKeys));
    }

    static FilterProvider ignoreSerializedSize() {
        return createFilterProvider("serializedKeySize", "serializedValueSize");
    }

    static FilterProvider ignoreHeaders() {
        return createFilterProvider("serializedKeySize", "serializedValueSize", "headers");
    }

    @Override
    default ObjectMapper getMapper() {
        return MAPPER;
    }

    @JsonIgnoreType
    abstract class ByteBufferIgnoreMixin {}


    @JsonFilter("kafkaRecordIgnores")
    class ConsumerRecordMixin<K, V> extends ConsumerRecord<K, V> implements RecordMixin {

        @JsonCreator
        ConsumerRecordMixin(@JsonProperty(value = "topic", required = true) String topic,
                            @JsonProperty(value = "partition", required = true) int partition,
                            @JsonProperty(value = "offset", required = true) long offset,
                            @JsonProperty("timestamp") long timestamp,
                            @JsonProperty("timestampType") TimestampType timestampType,
                            @JsonProperty("checksum") Long checksum,
                            @JsonProperty("serializedKeySize") int serializedKeySize,
                            @JsonProperty("serializedValueSize") int serializedValueSize, @JsonProperty("key") K key,
                            @JsonProperty("value") V value, @JsonProperty("headers") HeadersMixin headers,
                            @JsonProperty("leaderEpoch") Integer leaderEpoch) {
            super(topic, partition, offset, timestamp, timestampType,
                  Objects.isNull(checksum) || checksum < 0
                  ? DefaultRecord.computePartialChecksum(timestamp, serializedKeySize, serializedValueSize)
                  : checksum, serializedKeySize, serializedValueSize, key, value, headers,
                  Optional.ofNullable(leaderEpoch));
        }

        @JsonProperty("checksum")
        @Override
        public long checksum() {
            return super.checksum();
        }

    }


    @JsonFilter("kafkaRecordIgnores")
    class ProducerRecordMixin<K, V> extends ProducerRecord<K, V> implements RecordMixin {

        @JsonCreator
        ProducerRecordMixin(@JsonProperty(value = "topic", required = true) String topic,
                            @JsonProperty(value = "partition", required = true) Integer partition,
                            @JsonProperty("key") K key, @JsonProperty("value") V value,
                            @JsonProperty("timestamp") Long timestamp, @JsonProperty("headers") HeadersMixin headers) {
            super(topic, partition, timestamp, key, value, headers);
        }

    }


    class HeadersMixin extends RecordHeaders implements RecordMixin {

        @JsonCreator
        HeadersMixin(@JsonProperty("headers") HeaderMixin[] headers, @JsonProperty("isReadOnly") boolean readOnly) {
            super(headers);
            if (readOnly) {
                this.setReadOnly();
            }
        }

    }


    class HeaderMixin extends RecordHeader implements RecordMixin {

        @JsonCreator
        HeaderMixin(@JsonProperty("key") String key, @JsonProperty("value") byte[] value) {
            super(key, value);
        }

        public static HeaderMixin from(RecordHeader record) {
            return new HeaderMixin(record.key(), record.value());
        }

    }

}
