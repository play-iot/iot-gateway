package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public final class UnsignedIntegerSerializer extends EncodableSerializer<UnsignedInteger> {

    UnsignedIntegerSerializer() {
        super(UnsignedInteger.class);
    }

    @Override
    public void serialize(UnsignedInteger value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.longValue());
    }

}
