package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.github.zero88.utils.DateTimes.Iso8601Parser;
import io.github.zero88.utils.Functions;

import com.serotonin.bacnet4j.type.constructed.DateTime;

import lombok.NonNull;

public final class DateTimeDeserializer implements EncodableDeserializer<DateTime, String> {

    @Override
    public @NonNull Class<DateTime> encodableClass() {
        return DateTime.class;
    }

    @Override
    public @NonNull Class<String> javaClass() {
        return String.class;
    }

    @Override
    public DateTime parse(@NonNull String value) {
        return Functions.getIfThrow(() -> Iso8601Parser.parseDateTime(value))
                        .map(d -> new DateTime(DateDeserializer.create(d), TimeDeserializer.create(d.toOffsetTime())))
                        .orElse(DateTime.UNSPECIFIED);
    }

}
