package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.DateRange;
import com.serotonin.bacnet4j.type.primitive.Date;

import lombok.NonNull;

public final class DateRangeDeserializer implements EncodableDeserializer<DateRange, JsonObject> {

    @Override
    public @NonNull Class<DateRange> encodableClass() {
        return DateRange.class;
    }

    @Override
    public @NonNull Class<JsonObject> javaClass() {
        return JsonObject.class;
    }

    @Override
    public DateRange parse(@NonNull JsonObject value) {
        final DateDeserializer lookup = (DateDeserializer) EncodableDeserializerRegistry.lookup(Date.class);
        final Date startDate = lookup.parse(value.getString("startDate"));
        final Date endDate = lookup.parse(value.getString("endDate"));
        return new DateRange(startDate, endDate);
    }

}
