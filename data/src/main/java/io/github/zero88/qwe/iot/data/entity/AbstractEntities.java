package io.github.zero88.qwe.iot.data.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.github.zero88.qwe.iot.data.IoTEntities;
import io.github.zero88.qwe.iot.data.IoTEntity;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

public abstract class AbstractEntities<K, T extends IoTEntity<K>> implements IoTEntities<K, T> {

    private final Map<K, T> data = new HashMap<>();

    @Override
    public IoTEntities<K, T> add(T ioTEntity) {
        data.put(ioTEntity.key(), ioTEntity);
        return this;
    }

    @Override
    public Collection<T> entities() {
        return data.values();
    }

    @Override
    public JsonObject toJson(@NonNull ObjectMapper mapper) {
        return mapper.convertValue(data, JsonObject.class);
    }

}
