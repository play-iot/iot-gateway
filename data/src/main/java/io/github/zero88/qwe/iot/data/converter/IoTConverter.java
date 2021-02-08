package io.github.zero88.qwe.iot.data.converter;

import java.io.Serializable;

import io.github.zero88.qwe.iot.data.IoTObject;
import io.github.zero88.qwe.protocol.HasProtocol;
import io.github.zero88.qwe.protocol.Protocol;

import lombok.NonNull;

/**
 * Represents a {@code converter} between two equivalent types of {@code IoT object} with different protocol
 *
 * @param <T> Type of IoT object
 * @param <U> Type of IoT object
 * @see Protocol
 * @see IoTObject
 * @since 1.0.0
 */
public interface IoTConverter<T, U> extends HasProtocol, Serializable {

    /**
     * Translate a {@code protocol} object to a {@code QWE IoT concept}
     *
     * @param object {@code protocol} object
     * @return The QWE IoT concept
     * @apiNote if cannot translate, output can be {@code null} or throw {@link IllegalArgumentException} depends on
     *     detail implementation
     * @since 1.0.0
     */
    T serialize(U object);

    /**
     * Translate {@code QWE IoT concept} to a {@code protocol} object
     *
     * @param concept The QWE IoT concept
     * @return The protocol object
     * @apiNote if cannot translate, output can be {@code null} or throw {@link IllegalArgumentException} depends on
     *     detail implementation
     * @since 1.0.0
     */
    U deserialize(T concept);

    /**
     * The {@code QWE IoT concept} type
     *
     * @return the class
     * @since 1.0.0
     */
    @NonNull Class<T> fromType();

    /**
     * The {@code protocol} object type
     *
     * @return the class
     * @since 1.0.0
     */
    @NonNull Class<U> toType();

}
