package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public final class ObjectIdentifierValueSerializer extends EncodableSerializer<ObjectIdentifier>
    implements ObjectIdentifierMixin {

    ObjectIdentifierValueSerializer() {
        super(ObjectIdentifier.class);
    }

    @Override
    public void serialize(ObjectIdentifier value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(ObjectIdentifierMixin.serialize(value));
    }

}
