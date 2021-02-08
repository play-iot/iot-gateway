package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.StatusFlags;

import lombok.NonNull;

public final class StatusFlagsDeserializer extends BitStringDeserializer<StatusFlags> {

    StatusFlagsDeserializer() {
        super(StatusFlags.class);
    }

    @Override
    public StatusFlags parse(@NonNull JsonObject values) {
        return new StatusFlags(values.getBoolean("in-alarm", false), values.getBoolean("fault", false),
                               values.getBoolean("overridden", false), values.getBoolean("out-of-service", false));
    }

}
