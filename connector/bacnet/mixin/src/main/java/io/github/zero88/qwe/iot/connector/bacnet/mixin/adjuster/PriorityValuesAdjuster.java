package io.github.zero88.qwe.iot.connector.bacnet.mixin.adjuster;

import java.util.Objects;
import java.util.function.BiFunction;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer.EncodableSerializer;
import io.github.zero88.utils.Functions;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer.EncodableDeserializer;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer.EncodableDeserializerRegistry;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.primitive.Null;

public final class PriorityValuesAdjuster implements BiFunction<Encodable, PriorityArray, PriorityArray> {

    @Override
    @SuppressWarnings("unchecked")
    public PriorityArray apply(Encodable presentValue, PriorityArray arrayValues) {
        if (Objects.isNull(presentValue) || presentValue instanceof Null || Objects.isNull(arrayValues)) {
            return arrayValues;
        }
        final Class<? extends Encodable> clazz = presentValue.getClass();
        final EncodableDeserializer deserializer = EncodableDeserializerRegistry.lookup(clazz);
        int index = 0;
        for (PriorityValue pv : arrayValues.getValues()) {
            index++;
            final Encodable value = pv.getConstructedValue();
            if (value instanceof Null || clazz.isAssignableFrom(value.getClass())) {
                continue;
            }
            arrayValues.put(index, Functions.getOrDefault(value, () -> deserializer.parse(
                deserializer.cast(EncodableSerializer.encode(value)))));
        }
        return arrayValues;
    }

}
