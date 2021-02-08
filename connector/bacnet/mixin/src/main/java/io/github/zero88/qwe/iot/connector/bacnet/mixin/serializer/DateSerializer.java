package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import io.github.zero88.utils.DateTimes;
import io.github.zero88.utils.DateTimes.Iso8601Formatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.primitive.Date;

public final class DateSerializer extends EncodableSerializer<Date> {

    DateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Object v = value.isSpecific() ? Iso8601Formatter.formatDate(
            DateTimes.toUTC(value.calculateGC().toInstant()).toOffsetDateTime()) : value.toString();
        gen.writeObject(v);
    }

}
