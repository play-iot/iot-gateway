package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import java.util.Objects;
import java.util.Optional;

import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyValues;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@Builder(builderClassName = "Builder")
public final class RemoteDeviceMixin implements BACnetJsonMixin {

    @JsonIgnore
    private final ObjectIdentifier objectId;
    private final String name;
    private final AddressMixin address;
    @JsonIgnore
    private final PropertyValuesMixin propertyValues;

    public static RemoteDeviceMixin create(@NonNull RemoteDevice device) {
        return RemoteDeviceMixin.builder()
                                .objectId(device.getObjectIdentifier())
                                .name(device.getName())
                                .address(AddressMixin.create(device.getAddress()))
                                .build();
    }

    public static RemoteDeviceMixin create(@NonNull RemoteDevice device, PropertyValuesMixin values) {
        return create(device.getObjectIdentifier(), device.getAddress(), values);
    }

    public static RemoteDeviceMixin create(@NonNull ObjectIdentifier objectId, @NonNull Address address,
                                           @NonNull PropertyValues values) {
        return create(objectId, address, PropertyValuesMixin.create(objectId, values, false));
    }

    public static RemoteDeviceMixin create(@NonNull ObjectIdentifier objectId, @NonNull Address address,
                                           @NonNull PropertyValuesMixin mixin) {
        return new RemoteDeviceMixin(objectId, mixin.encode(PropertyIdentifier.objectName),
                                     AddressMixin.create(address), mixin);
    }

    public static RemoteDeviceMixin parse(@NonNull ObjectIdentifier objectId, JsonObject address,
                                          JsonObject properties) {
        return new RemoteDeviceMixin(objectId, properties.getString("name"),
                                     Optional.ofNullable(address).map(AddressMixin::create).orElse(null),
                                     PropertyValuesMixin.create(properties));
    }

    public int getInstanceNumber() {
        return getObjectId().getInstanceNumber();
    }

    @Override
    public JsonObject toJson() {
        final JsonObject entries = getMapper().convertValue(this, JsonObject.class);
        if (Objects.isNull(propertyValues)) {
            return entries;
        }
        return entries.mergeIn(propertyValues.toJson());
    }

}
