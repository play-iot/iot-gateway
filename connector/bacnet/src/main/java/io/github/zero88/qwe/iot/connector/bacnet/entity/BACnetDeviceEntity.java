package io.github.zero88.qwe.iot.connector.bacnet.entity;

import java.util.Objects;

import io.github.zero88.qwe.iot.connector.bacnet.converter.property.DeviceStatusConverter;
import io.github.zero88.qwe.iot.data.entity.AbstractDevice;
import io.github.zero88.qwe.iot.data.enums.DeviceStatus;
import io.github.zero88.qwe.iot.data.enums.DeviceType;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.PropertyValuesMixin;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.RemoteDeviceMixin;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@Accessors(fluent = true)
public class BACnetDeviceEntity extends AbstractDevice<ObjectIdentifier> implements BACnetEntity<ObjectIdentifier> {

    @NonNull
    @JsonIgnore
    private final RemoteDeviceMixin mixin;

    public static BACnetDeviceEntity from(@NonNull String networkId, @NonNull RemoteDeviceMixin mixin) {
        final PropertyValuesMixin values = mixin.getPropertyValues();
        final DeviceType type = Objects.nonNull(values.encode(PropertyIdentifier.deviceAddressBinding))
                                ? DeviceType.GATEWAY
                                : DeviceType.factory(values.encode(PropertyIdentifier.deviceType));
        final DeviceStatus status = new DeviceStatusConverter().serialize(
            (com.serotonin.bacnet4j.type.enumerated.DeviceStatus) values.getAndCast(PropertyIdentifier.systemStatus)
                                                                        .orElse(
                                                                            com.serotonin.bacnet4j.type.enumerated.DeviceStatus.nonOperational));
        return BACnetDeviceEntity.builder()
                                 .networkId(networkId)
                                 .key(mixin.getObjectId())
                                 .name(mixin.getName())
                                 .address(mixin.getAddress().toJson())
                                 .type(type)
                                 .status(status)
                                 .mixin(mixin)
                                 .build();
    }

    @Override
    public JsonObject toJson(@NonNull ObjectMapper mapper) {
        final JsonObject json = super.toJson(mapper);
        return json.mergeIn(mixin.toJson());
    }

}
