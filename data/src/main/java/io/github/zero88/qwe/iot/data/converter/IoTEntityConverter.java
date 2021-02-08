package io.github.zero88.qwe.iot.data.converter;

import io.github.zero88.qwe.iot.data.IoTEntity;

/**
 * Represents a {@code converter} between two equivalent types of {@code IoT entity} with different protocol
 *
 * @param <T> Type of IoT entity
 * @param <U> Type of IoT entity
 * @since 1.0.0
 */
public interface IoTEntityConverter<T extends IoTEntity, U> extends IoTConverter<T, U> {

}
