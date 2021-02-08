package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import java.time.OffsetTime;

import io.github.zero88.utils.DateTimes.Iso8601Parser;
import io.github.zero88.utils.Functions;

import com.serotonin.bacnet4j.type.primitive.Time;

import lombok.NonNull;

public final class TimeDeserializer implements EncodableDeserializer<Time, String> {

    static Time create(@NonNull OffsetTime time) {
        return new Time(time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
    }

    @Override
    public @NonNull Class<Time> encodableClass() {
        return Time.class;
    }

    @Override
    public @NonNull Class<String> javaClass() {
        return String.class;
    }

    @Override
    public Time parse(@NonNull String value) {
        return Functions.getIfThrow(() -> Iso8601Parser.parseTime(value))
                        .map(TimeDeserializer::create)
                        .orElse(Time.UNSPECIFIED);
    }

}
