package io.github.zero88.iot.connector.kafka.serialization;

import java.util.Map;
import java.util.Objects;

import org.apache.kafka.common.serialization.Serializer;

import io.github.zero88.qwe.event.EventMessage;

public final class EventMessageSerializer implements Serializer<EventMessage> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) { }

    @Override
    public byte[] serialize(String topic, EventMessage data) {
        return Objects.isNull(data) ? null : data.toJson().encode().getBytes();
    }

    @Override
    public void close() { }

}
