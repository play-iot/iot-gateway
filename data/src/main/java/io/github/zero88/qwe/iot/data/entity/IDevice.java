package io.github.zero88.qwe.iot.data.entity;

import java.util.Collection;
import java.util.Collections;

import io.github.zero88.qwe.iot.data.IoTEntity;
import io.github.zero88.qwe.iot.data.enums.DeviceStatus;
import io.github.zero88.qwe.iot.data.enums.DeviceType;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

/**
 * Represents for a {@code semantic IoT device} entity
 *
 * @param <K> Type of device key
 */
public interface IDevice<K> extends IoTEntity<K>, HasObjectType<DeviceType> {

    /**
     * Retrieve a network identifier that device belongs to
     *
     * @return network identifier
     */
    @NonNull String networkId();

    /**
     * Device address
     *
     * @return device address
     */
    @NonNull JsonObject address();

    /**
     * Device name
     *
     * @return device name
     */
    @NonNull String name();

    /**
     * Device status
     *
     * @return device status
     */
    @NonNull DeviceStatus status();

    /**
     * Return list of points that belongs to network
     *
     * @return list of points, might empty
     * @see IPoint
     */
    default @NonNull Collection<IPoint> points() {
        return Collections.emptyList();
    }

}
