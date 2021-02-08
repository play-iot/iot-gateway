package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import io.github.zero88.utils.DateTimes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.DateTime;

public final class DateTimeSerializer extends EncodableSerializer<DateTime> {

    DateTimeSerializer() {
        super(DateTime.class);
    }

    @Override
    public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        final Object v = value.isFullySpecified() ? DateTimes.toUTC(value.getGC().toInstant()) : value.toString();
        gen.writeObject(v);
    }

}
