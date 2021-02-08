package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.DateRange;
import com.serotonin.bacnet4j.type.primitive.Date;

public final class DateRangeSerializer extends EncodableSerializer<DateRange> {

    DateRangeSerializer() {
        super(DateRange.class);
    }

    @Override
    public void serialize(DateRange value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Date> map = new HashMap<>();
        map.put("startDate", value.getStartDate());
        map.put("endDate", value.getEndDate());
        gen.writeObject(map);
    }

}
