package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.primitive.Null;

public final class NullSerializer extends EncodableSerializer<Null> {

    NullSerializer() {
        super(Null.class);
    }

    @Override
    public void serialize(Null value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNull();
    }

}
