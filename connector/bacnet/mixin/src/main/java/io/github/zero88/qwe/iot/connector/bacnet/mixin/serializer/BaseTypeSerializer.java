package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import io.github.zero88.utils.Functions;
import io.github.zero88.utils.Reflections;
import io.github.zero88.utils.Reflections.ReflectionClass;
import io.github.zero88.utils.Reflections.ReflectionField;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BaseType;

public final class BaseTypeSerializer extends EncodableSerializer<BaseType> {

    BaseTypeSerializer() {
        super(BaseType.class);
    }

    @Override
    public void serialize(BaseType value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(BaseType v, JsonGenerator g) throws IOException {
        g.writeObject(toObject(v));
    }

    private Map<String, Encodable> toObject(BaseType value) {
        return ReflectionField.stream(value.getClass(), check())
                              .map(f -> new SimpleEntry<>(BACnetJsonMixin.standardizeKey(f.getName()),
                                                          ReflectionField.getFieldValue(value, f, Encodable.class)))
                              .filter(entry -> Objects.nonNull(entry.getValue()))
                              .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    private Predicate<Field> check() {
        return Functions.and(Reflections.notModifiers(Modifier.STATIC), Reflections.hasModifiers(Modifier.PRIVATE),
                             f -> ReflectionClass.assertDataType(f.getType(), Encodable.class));
    }

}
