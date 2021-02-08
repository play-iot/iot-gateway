package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import java.time.OffsetDateTime;

import io.github.zero88.utils.DateTimes.Iso8601Parser;
import io.github.zero88.utils.Functions;

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.type.primitive.Date;

import lombok.NonNull;

public final class DateDeserializer implements EncodableDeserializer<Date, String> {

    static Date create(OffsetDateTime d) {
        return new Date(d.getYear(), Month.valueOf(d.getMonth().getValue()), d.getDayOfMonth(),
                        DayOfWeek.valueOf(d.getDayOfWeek().getValue()));
    }

    @Override
    public @NonNull Class<Date> encodableClass() {
        return Date.class;
    }

    @Override
    public @NonNull Class<String> javaClass() {
        return String.class;
    }

    @Override
    public Date parse(@NonNull String value) {
        return Functions.getIfThrow(() -> Iso8601Parser.parseDate(value))
                        .map(DateDeserializer::create)
                        .orElse(Date.UNSPECIFIED);
    }

}
