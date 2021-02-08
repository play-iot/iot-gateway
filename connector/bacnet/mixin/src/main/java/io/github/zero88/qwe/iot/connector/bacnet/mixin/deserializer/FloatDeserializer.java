package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.primitive.Real;

import lombok.NonNull;

public final class FloatDeserializer implements NumberDeserializer<Real, Float> {

    @Override
    public @NonNull Class<Real> encodableClass() {
        return Real.class;
    }

    @Override
    public @NonNull Class<Float> javaClass() {
        return Float.class;
    }

    @Override
    public Float cast(@NonNull Object value) {
        return NumberDeserializer.castToFloat(value);
    }

    @Override
    public Real parse(@NonNull Float value) {
        return new Real(value);
    }

}
