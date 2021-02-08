package io.github.zero88.qwe.iot.connector.bacnet.discovery;

import io.github.zero88.qwe.iot.connector.Subject;
import io.vertx.core.json.JsonObject;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public final class DiscoveryArguments implements Subject {

    @NonNull
    private final DiscoveryParams params;
    @NonNull
    private final DiscoveryOptions options;
    @NonNull
    private final DiscoveryLevel level;

    @Override
    public String key() {
        return params().buildKey(level);
    }

    @Override
    public JsonObject toDetail() {
        return params().toJson().put("key", key());
    }

}
