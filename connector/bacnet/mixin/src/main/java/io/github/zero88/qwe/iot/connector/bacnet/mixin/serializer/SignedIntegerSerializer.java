package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;

public final class SignedIntegerSerializer extends EncodableSerializer<SignedInteger> {

    SignedIntegerSerializer() {
        super(SignedInteger.class);
    }

    @Override
    public void serialize(SignedInteger value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.longValue());
    }

}
