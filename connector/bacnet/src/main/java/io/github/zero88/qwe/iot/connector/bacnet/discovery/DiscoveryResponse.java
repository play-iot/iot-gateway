package io.github.zero88.qwe.iot.connector.bacnet.discovery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetDeviceEntity;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetEntities.BACnetPoints;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetPointEntity;
import io.github.zero88.qwe.iot.connector.bacnet.internal.LocalDeviceMetadata;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.vertx.core.json.JsonObject;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@FieldNameConstants
@Builder(builderClassName = "Builder")
@Deprecated
public final class DiscoveryResponse implements JsonData {

    private final CommunicationProtocol network;
    private final LocalDeviceMetadata localDevice;
    private final List<BACnetDeviceEntity> remoteDevices;
    private final BACnetDeviceEntity remoteDevice;
    private final BACnetPoints objects;
    private final BACnetPointEntity object;

    @Override
    public JsonObject toJson() {
        final JsonObject json = JsonData.super.toJson();
        Optional.ofNullable(object).ifPresent(o -> json.put(Fields.object, object.toJson()));
        Optional.ofNullable(objects).ifPresent(o -> json.put(Fields.objects, objects.toJson()));
        Optional.ofNullable(remoteDevice).ifPresent(o -> json.put(Fields.remoteDevice, remoteDevice.toJson()));
        Optional.ofNullable(remoteDevices)
                .ifPresent(o -> json.put(Fields.remoteDevices, remoteDevices.stream().map(BACnetDeviceEntity::toJson)
                                                                            .collect(Collectors.toList())));
        return json;
    }

}
