package io.github.zero88.qwe.iot.data.entity;

import io.github.zero88.qwe.iot.data.enums.DeviceStatus;
import io.github.zero88.qwe.iot.data.enums.DeviceType;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldNameConstants
@Accessors(fluent = true)
public abstract class AbstractDevice<K> implements IDevice<K> {

    @JsonProperty("_" + Fields.key)
    private final K key;
    @NonNull
    @JsonProperty("_" + Fields.networkId)
    private final String networkId;
    @NonNull
    @JsonProperty("_" + Fields.address)
    private final JsonObject address;
    @NonNull
    @JsonProperty("_" + Fields.type)
    private final DeviceType type;
    @NonNull
    @JsonProperty("_" + Fields.name)
    private final String name;
    @NonNull
    @JsonProperty("_" + Fields.status)
    private final DeviceStatus status;

}
