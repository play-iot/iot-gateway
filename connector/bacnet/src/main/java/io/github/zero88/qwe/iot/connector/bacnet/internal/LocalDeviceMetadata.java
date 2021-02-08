package io.github.zero88.qwe.iot.connector.bacnet.internal;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetConfig;
import io.vertx.core.shareddata.Shareable;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public final class LocalDeviceMetadata implements JsonData, Shareable {

    private final int vendorId;
    private final String vendorName;
    private final int deviceNumber;
    private final String deviceName;
    private final String modelName;

    public static LocalDeviceMetadata from(@NonNull BACnetConfig config) {
        return LocalDeviceMetadata.builder()
                                  .vendorId(config.getVendorId())
                                  .vendorName(config.getVendorName())
                                  .deviceNumber(config.getDeviceId())
                                  .deviceName(config.getDeviceName())
                                  .modelName(config.getModelName())
                                  .build();
    }

}
