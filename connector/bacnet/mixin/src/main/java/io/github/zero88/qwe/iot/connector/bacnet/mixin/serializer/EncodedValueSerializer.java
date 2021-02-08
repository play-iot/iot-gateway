package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.EncodedValue;

public final class EncodedValueSerializer extends EncodableSerializer<EncodedValue> {

    EncodedValueSerializer() {
        super(EncodedValue.class);
    }

    @Override
    public void serialize(EncodedValue value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(EncodedValue value, JsonGenerator gen) throws IOException {
        gen.writeString(Base64.getEncoder().encodeToString(value.getData()));
    }

}
