package io.github.zero88.qwe.iot.connector.bacnet.discovery;

import java.util.Objects;
import java.util.Optional;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.utils.Strings;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@FieldNameConstants
@Builder(builderClassName = "Builder")
public final class DiscoveryParams implements JsonData {

    private final JsonObject network;
    private final String networkId;
    private final Integer deviceInstance;
    private final String objectCode;

    public ObjectIdentifier remoteDeviceId() {
        return Optional.ofNullable(deviceInstance)
                       .map(number -> new ObjectIdentifier(ObjectType.device, number))
                       .orElse(null);
    }

    public ObjectIdentifier objectCode() {
        return Optional.ofNullable(objectCode).map(code -> ObjectIdentifierMixin.deserialize(objectCode)).orElse(null);
    }

    public String buildKey(DiscoveryLevel level) {
        String key = networkId;
        if (DiscoveryLevel.DEVICE.mustValidate(level)) {
            key += "_" + deviceInstance;
        }
        if (DiscoveryLevel.OBJECT.mustValidate(level)) {
            key += "_" + objectCode;
        }
        return key;
    }

    @NonNull
    public static DiscoveryParams from(@NonNull RequestData requestData, @NonNull DiscoveryLevel level) {
        return from(Optional.ofNullable(requestData.body()).orElse(new JsonObject()), level);
    }

    @NonNull
    public static DiscoveryParams from(@NonNull JsonObject body, @NonNull DiscoveryLevel level) {
        return validate(JsonData.from(body, DiscoveryParams.class, JsonData.LENIENT_MAPPER), level);
    }

    @NonNull
    public static DiscoveryParams validate(@NonNull DiscoveryParams request, @NonNull DiscoveryLevel level) {
        if (DiscoveryLevel.NETWORK.mustValidate(level)) {
            Strings.requireNotBlank(request.getNetworkId(), "Missing BACnet network code");
        }
        if (DiscoveryLevel.DEVICE.mustValidate(level)) {
            Objects.requireNonNull(request.getDeviceInstance(), "Missing BACnet device code");
        }
        if (DiscoveryLevel.OBJECT.mustValidate(level)) {
            Strings.requireNotBlank(request.getObjectCode(), "Missing BACnet object code");
        }
        return request;
    }

    public static String genServicePath(@NonNull DiscoveryLevel level) {
        if (level == DiscoveryLevel.NETWORK) {
            return "/network";
        }
        if (level == DiscoveryLevel.DEVICE) {
            return "/network/:" + Fields.networkId + "/device";
        }
        return "/network/:" + Fields.networkId + "/device/:" + Fields.deviceInstance + "/object";
    }

    public static String genParamPath(DiscoveryLevel level) {
        if (level == DiscoveryLevel.NETWORK) {
            return Fields.networkId;
        }
        if (level == DiscoveryLevel.DEVICE) {
            return Fields.deviceInstance;
        }
        return Fields.objectCode;
    }

}
