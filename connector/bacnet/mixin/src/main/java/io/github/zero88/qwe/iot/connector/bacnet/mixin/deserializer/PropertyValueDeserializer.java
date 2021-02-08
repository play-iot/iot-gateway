package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.PriorityValue;

import lombok.NonNull;

//TODO implement it
public class PropertyValueDeserializer implements EncodableDeserializer<PriorityValue, JsonObject> {

    @Override
    public @NonNull Class<PriorityValue> encodableClass() {
        return PriorityValue.class;
    }

    @Override
    public @NonNull Class<JsonObject> javaClass() {
        return JsonObject.class;
    }

    @Override
    public PriorityValue parse(@NonNull JsonObject value) {
        return null;
    }

}
