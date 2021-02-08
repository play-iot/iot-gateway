package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Enumerated;

public final class EnumeratedSerializer extends EncodableSerializer<Enumerated> {

    private final List<Class<? extends Enumerated>> ignores = Arrays.asList(ObjectType.class, PropertyIdentifier.class,
                                                                            ErrorCode.class, ErrorClass.class);

    EnumeratedSerializer() {
        super(Enumerated.class);
    }

    @Override
    public void serialize(Enumerated value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(Enumerated value, JsonGenerator gen) throws IOException {
        if (ignores.stream().anyMatch(clazz -> clazz.isInstance(value))) {
            gen.writeString(value.toString());
            return;
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("value", value.toString());
        map.put("rawValue", value.intValue());
        gen.writeObject(map);
    }

}
