package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public final class ObjectIdentifierKeySerializer extends JsonSerializer<ObjectIdentifier>
    implements ObjectIdentifierMixin {

    @Override
    public void serialize(ObjectIdentifier value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeFieldName(ObjectIdentifierMixin.serialize(value));
    }

}
