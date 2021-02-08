package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.BaseType;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class BaseTypeDeserializer<T extends BaseType> implements EncodableDeserializer<T, JsonObject> {

    @NonNull
    private final Class<T> clazz;

    @Override
    public @NonNull Class<T> encodableClass() {
        return clazz;
    }

    @Override
    public @NonNull Class<JsonObject> javaClass() {
        return JsonObject.class;
    }

}
