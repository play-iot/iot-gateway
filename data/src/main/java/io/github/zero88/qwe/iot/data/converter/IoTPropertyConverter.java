package io.github.zero88.qwe.iot.data.converter;

import io.github.zero88.qwe.iot.data.IoTProperty;

/**
 * Represents a {@code converter} between two equivalent types of {@code IoT entity} with different protocol
 *
 * @param <T> Type of IoT property
 * @param <U> Type of IoT property or mixin from specific protocol
 * @see IoTProperty
 * @since 1.0.0
 */
public interface IoTPropertyConverter<T extends IoTProperty, U> extends IoTConverter<T, U> {

}
