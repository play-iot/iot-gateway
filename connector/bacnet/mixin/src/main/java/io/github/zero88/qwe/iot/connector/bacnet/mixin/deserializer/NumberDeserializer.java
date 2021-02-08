package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import java.util.function.Function;

import com.serotonin.bacnet4j.type.Encodable;

import lombok.NonNull;

public interface NumberDeserializer<T extends Encodable, V extends Number> extends EncodableDeserializer<T, V> {

    static Integer castToInt(@NonNull Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return tryParse(value, Integer::parseInt);
    }

    static Long castToLong(@NonNull Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return tryParse(value, Long::parseLong);
    }

    static Double castToDouble(@NonNull Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return tryParse(value, Double::parseDouble);
    }

    static Float castToFloat(@NonNull Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return tryParse(value, Float::parseFloat);
    }

    static Short castToShort(@NonNull Object value) {
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        return tryParse(value, Short::parseShort);
    }

    static <T extends Number> T tryParse(@NonNull Object value, Function<String, T> parser) {
        if (value instanceof String) {
            return parser.apply((String) value);
        }
        throw new IllegalArgumentException("Invalid number: " + value);
    }

}
