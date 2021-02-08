package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.ObjectTypesSupported;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;

public final class ObjectTypesSupportedSerializer extends EncodableSerializer<ObjectTypesSupported> {

    ObjectTypesSupportedSerializer() {
        super(ObjectTypesSupported.class);
    }

    @Override
    public void serialize(ObjectTypesSupported value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(ObjectTypesSupported value, JsonGenerator gen) throws IOException {
        gen.writeObject(IntStream.range(0, ObjectType.size())
                                 .boxed()
                                 .map(ObjectType::forId)
                                 .collect(Collectors.toMap(ObjectType::toString, value::is)));
    }

}
