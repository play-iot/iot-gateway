package io.github.zero88.qwe.iot.connector.bacnet.converter.property;

import java.util.Objects;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetProtocol;
import io.github.zero88.qwe.iot.data.converter.IoTPropertyConverter;
import io.github.zero88.qwe.iot.data.enums.DeviceStatus;

import lombok.NonNull;

public class DeviceStatusConverter
    implements IoTPropertyConverter<DeviceStatus, com.serotonin.bacnet4j.type.enumerated.DeviceStatus>, BACnetProtocol {

    @Override
    public DeviceStatus serialize(com.serotonin.bacnet4j.type.enumerated.DeviceStatus object) {
        if (Objects.isNull(object)) {
            return DeviceStatus.UNKNOWN;
        }
        if (com.serotonin.bacnet4j.type.enumerated.DeviceStatus.nonOperational.equals(object)) {
            return DeviceStatus.DOWN;
        }
        if (com.serotonin.bacnet4j.type.enumerated.DeviceStatus.backupInProgress.equals(object) ||
            com.serotonin.bacnet4j.type.enumerated.DeviceStatus.downloadInProgress.equals(object)) {
            return DeviceStatus.parse("BUSY_" + object.toString());
        }
        return DeviceStatus.UP;
    }

    @Override
    public com.serotonin.bacnet4j.type.enumerated.DeviceStatus deserialize(DeviceStatus concept) {
        if (Objects.isNull(concept) || concept.equals(DeviceStatus.UNKNOWN)) {
            return null;
        }
        if (DeviceStatus.DOWN.equals(concept)) {
            return com.serotonin.bacnet4j.type.enumerated.DeviceStatus.nonOperational;
        }
        if (concept.status().contains("BUSY_")) {
            String s = concept.status().replace("BUSY_", "");
            return com.serotonin.bacnet4j.type.enumerated.DeviceStatus.forName(s);
        }
        return com.serotonin.bacnet4j.type.enumerated.DeviceStatus.operational;
    }

    @Override
    public @NonNull Class<DeviceStatus> fromType() {
        return DeviceStatus.class;
    }

    @Override
    public @NonNull Class<com.serotonin.bacnet4j.type.enumerated.DeviceStatus> toType() {
        return com.serotonin.bacnet4j.type.enumerated.DeviceStatus.class;
    }

}
