package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Objects;

import io.reactivex.functions.BiConsumer;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.type.Encodable;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class EncodableSerializer<T extends Encodable> extends StdSerializer<T> {

    public static final EncodableSerializer<Encodable> DEFAULT = new EncodableSerializer<Encodable>(Encodable.class) {};

    EncodableSerializer(@NonNull Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T encode(Encodable encodable) {
        if (Objects.isNull(encodable)) {
            return null;
        }
        return (T) BACnetJsonMixin.MAPPER.convertValue(Collections.singletonMap("encode", encodable), JsonObject.class)
                                         .stream()
                                         .map(Entry::getValue)
                                         .findFirst()
                                         .orElse(null);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        defaultSerialize(value, gen);
    }

    private void defaultSerialize(T value, JsonGenerator gen) throws IOException {
        gen.writeString(value.toString());
    }

    void serializeIfAnyErrorFallback(BiConsumer<T, JsonGenerator> write, T v, JsonGenerator gen) throws IOException {
        try {
            write.accept(v, gen);
        } catch (Exception e) {
            log.warn("Fallback to default serialize", e);
            defaultSerialize(v, gen);
        }
    }

}
