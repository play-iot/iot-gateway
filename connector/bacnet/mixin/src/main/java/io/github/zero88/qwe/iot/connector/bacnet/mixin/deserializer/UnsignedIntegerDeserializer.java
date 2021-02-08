package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import lombok.NonNull;

public final class UnsignedIntegerDeserializer implements NumberDeserializer<UnsignedInteger, Long> {

    @Override
    public @NonNull Class<UnsignedInteger> encodableClass() {
        return UnsignedInteger.class;
    }

    @Override
    public @NonNull Class<Long> javaClass() {
        return Long.class;
    }

    @Override
    public Long cast(@NonNull Object value) {
        return NumberDeserializer.castToLong(value);
    }

    @Override
    public UnsignedInteger parse(@NonNull Long value) {
        return new UnsignedInteger(value);
    }

}
