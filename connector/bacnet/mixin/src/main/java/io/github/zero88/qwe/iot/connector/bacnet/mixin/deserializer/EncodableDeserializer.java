package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.converter.CarlExceptionConverter;
import io.github.zero88.utils.Functions;
import io.github.zero88.utils.Strings;

import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.ObjectPropertyTypeDefinition;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;

/**
 * The interface Encodable deserializer.
 *
 * @param <T> Type of {@code Encodable}
 * @param <V> Type of {@code Java object}
 * @since 1.0.0
 */
public interface EncodableDeserializer<T extends Encodable, V> {

    /**
     * The constant LOGGER.
     */
    Logger LOGGER = LoggerFactory.getLogger(EncodableDeserializer.class);

    /**
     * Parse value based on property identifier
     *
     * @param propertyIdentifier Given property identifier
     * @param value              Given value
     * @return BACnet Encodable value
     * @throws CarlException if catching any error when parsing
     * @since 1.0.0
     */
    static Encodable parse(@NonNull PropertyIdentifier propertyIdentifier, Object value) {
        if (Objects.isNull(value)) {
            return Null.instance;
        }
        final PropertyTypeDefinition definition = ObjectProperties.getPropertyTypeDefinition(propertyIdentifier);
        if (Objects.isNull(definition)) {
            LOGGER.warn("Not found Encodable definition of {}", propertyIdentifier);
            return Null.instance;
        }
        final EncodableDeserializer deserializer = EncodableDeserializerRegistry.lookup(definition);
        if (definition.isCollection() && Objects.isNull(definition.getInnerType())) {
            return parse(value, definition, new SequenceOfDeserializer(definition, deserializer));
        }
        return parse(value, definition, deserializer);
    }

    /**
     * Parse value based on object identifier and property identifier
     *
     * @param objectIdentifier   the object identifier
     * @param propertyIdentifier Given property identifier
     * @param value              Given value
     * @return BACnet Encodable value
     * @throws CarlException if catching any error when parsing
     * @since 1.0.0
     */
    static Encodable parse(@NonNull ObjectIdentifier objectIdentifier, @NonNull PropertyIdentifier propertyIdentifier,
                           Object value) {
        if (Objects.isNull(value)) {
            return Null.instance;
        }
        final PropertyTypeDefinition definition = Optional.ofNullable(
            ObjectProperties.getObjectPropertyTypeDefinition(objectIdentifier.getObjectType(), propertyIdentifier))
                                                          .map(ObjectPropertyTypeDefinition::getPropertyTypeDefinition)
                                                          .orElse(null);
        if (Objects.isNull(definition)) {
            throw new IllegalArgumentException(
                "Object code " + objectIdentifier + " doesn't support " + propertyIdentifier);
        }
        final EncodableDeserializer deserializer = EncodableDeserializerRegistry.lookup(definition);
        if (definition.isCollection() && Objects.isNull(definition.getInnerType())) {
            return parse(value, definition, new SequenceOfDeserializer(definition, deserializer));
        }
        return parse(value, definition, deserializer);
    }

    /**
     * Parse encodable.
     *
     * @param value        the value
     * @param definition   the definition
     * @param deserializer the deserializer
     * @return the encodable
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    static Encodable parse(@NonNull Object value, @NonNull PropertyTypeDefinition definition,
                           @NonNull EncodableDeserializer deserializer) {
        return Functions.getOrThrow(() -> deserializer.parse(deserializer.cast(value)), t -> {
            final String msg = Strings.format("Cannot parse {0} ''{1}'' of ''{2}'' as data type {3}",
                                              definition.isCollection() ? "list item" : "value", value,
                                              definition.getPropertyIdentifier(), definition.getClazz().getName());
            if (t instanceof IllegalArgumentException && Objects.nonNull(t.getCause())) {
                return CarlExceptionConverter.friendly(t.getCause(), msg);
            }
            return CarlExceptionConverter.friendly(t, msg);
        });
    }

    /**
     * Same with {@link #parse(PropertyIdentifier, Object)} but with lenient that means returns {@link Null#instance} if
     * {@code value} is non-parsable
     *
     * @param propertyIdentifier Given property identifier
     * @param value              Given value
     * @return BACnet Encodable value
     * @see Null#instance
     * @since 1.0.0
     */
    static Encodable parseLenient(@NonNull PropertyIdentifier propertyIdentifier, Object value) {
        return Functions.getIfThrow(() -> parse(propertyIdentifier, value)).orElse(Null.instance);
    }

    /**
     * Defines Encodable class.
     *
     * @return the class
     * @since 1.0.0
     */
    @NonNull Class<T> encodableClass();

    /**
     * Defines java class.
     *
     * @return the class
     * @since 1.0.0
     */
    @NonNull Class<V> javaClass();

    /**
     * Cast given value to java object.
     *
     * @param value the value
     * @return the v
     * @since 1.0.0
     */
    @NonNull
    default V cast(@NonNull Object value) {
        return javaClass().cast(value);
    }

    /**
     * Parse t.
     *
     * @param value the value
     * @return the t
     * @since 1.0.0
     */
    T parse(@NonNull V value);

}
