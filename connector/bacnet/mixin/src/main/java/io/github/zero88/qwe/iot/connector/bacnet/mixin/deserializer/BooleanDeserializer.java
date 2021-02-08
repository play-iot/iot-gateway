package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.primitive.Boolean;

import lombok.NonNull;

public final class BooleanDeserializer implements EncodableDeserializer<Boolean, java.lang.Boolean> {

    @Override
    public @NonNull Class<Boolean> encodableClass() {
        return Boolean.class;
    }

    @Override
    public @NonNull Class<java.lang.Boolean> javaClass() {
        return java.lang.Boolean.class;
    }

    @Override
    public Boolean parse(java.lang.@NonNull Boolean value) {
        return Boolean.valueOf(value);
    }

}
