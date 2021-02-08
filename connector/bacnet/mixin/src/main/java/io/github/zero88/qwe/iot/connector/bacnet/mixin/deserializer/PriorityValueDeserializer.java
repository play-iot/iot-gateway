package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import java.util.Objects;

import io.github.zero88.utils.Reflections.ReflectionClass;

import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.Null;

import lombok.NonNull;

public final class PriorityValueDeserializer
    implements EncodableDeserializer<PriorityValue, Object>, NonRegistryDeserializer {

    @Override
    public @NonNull Class<PriorityValue> encodableClass() {
        return PriorityValue.class;
    }

    @Override
    public @NonNull Class<Object> javaClass() {
        return Object.class;
    }

    @Override
    public PriorityValue parse(@NonNull Object value) {
        if (Objects.isNull(value)) {
            return new PriorityValue(Null.instance);
        }
        if (ReflectionClass.assertDataType(value.getClass(), java.lang.Boolean.class)) {
            return new PriorityValue(Boolean.valueOf((java.lang.Boolean) value));
        }
        if (value instanceof Number) {
            final DoubleDeserializer deserializer = (DoubleDeserializer) EncodableDeserializerRegistry.lookup(
                Double.class);
            return new PriorityValue(deserializer.parse(deserializer.cast(value)));
        }
        if (value instanceof String) {
            return new PriorityValue(new CharacterString((String) value));
        }
        throw new IllegalArgumentException("Unsupported priority value type " + value.getClass());
    }

}
