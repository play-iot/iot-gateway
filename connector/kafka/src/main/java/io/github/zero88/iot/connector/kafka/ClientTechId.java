package io.github.zero88.iot.connector.kafka;

import java.util.Objects;

import org.apache.kafka.common.serialization.Serde;

import io.github.zero88.iot.connector.kafka.serialization.QWEKafkaSerdes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientTechId<K, V> {

    @Getter
    private final Class<K> keyClass;
    @Getter
    private final Serde<K> keySerdes;
    @Getter
    private final Class<V> valueClass;
    @Getter
    private final Serde<V> valueSerdes;
    @EqualsAndHashCode.Include
    private final String id;

    ClientTechId(@NonNull Class<K> keyClass, Serde<K> keySerdes, @NonNull Class<V> valueClass, Serde<V> valueSerdes) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.keySerdes = Objects.nonNull(keySerdes) ? keySerdes : QWEKafkaSerdes.serdeFrom(keyClass);
        this.valueSerdes = Objects.nonNull(valueSerdes) ? valueSerdes : QWEKafkaSerdes.serdeFrom(valueClass);
        this.id = kafkaClientIdentifier(keyClass, valueClass);
    }

    private static String kafkaClientIdentifier(@NonNull Class keyClass, @NonNull Class valueClass) {
        return keyClass.getName() + "::" + valueClass.getName();
    }

    @Override
    public String toString() { return id; }

}
