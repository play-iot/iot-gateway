package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.primitive.SignedInteger;

import lombok.NonNull;

public final class SignedIntegerDeserializer implements NumberDeserializer<SignedInteger, Long> {

    @Override
    public @NonNull Class<SignedInteger> encodableClass() {
        return SignedInteger.class;
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
    public SignedInteger parse(@NonNull Long value) {
        return new SignedInteger(value);
    }

}
