package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.github.zero88.utils.Functions;
import io.github.zero88.utils.Reflections.ReflectionMethod;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.type.primitive.BitString;

import lombok.NonNull;

public final class BitStringSerializer extends EncodableSerializer<BitString> {

    BitStringSerializer() {
        super(BitString.class);
    }

    @Override
    public void serialize(BitString value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(BitString value, JsonGenerator gen) throws IOException {
        final @NonNull Predicate<Method> predicate = m -> m.getReturnType() == boolean.class &&
                                                          m.getParameterCount() == 0 && m.getName().startsWith("is");
        final Map<String, Boolean> kv = ReflectionMethod.find(predicate, value.getClass())
                                                        .collect(Collectors.toMap(m -> BACnetJsonMixin.standardizeKey(
                                                            m.getName().substring(2)),
                                                                                  m -> Functions.getOrDefault(false,
                                                                                                              () -> (Boolean) m.invoke(
                                                                                                                  value))));
        if (kv.isEmpty()) {
            final boolean[] vs = value.getValue();
            gen.writeObject(
                IntStream.range(0, vs.length).boxed().collect(Collectors.toMap(String::valueOf, i -> vs[i])));
        } else {
            gen.writeObject(kv);
        }
    }

}
