package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import com.serotonin.bacnet4j.type.Encodable;

/**
 * Represents an {@code Encodable} that is able serialize to non-primitive java object, such as: JsonObject, JsonArray,
 * Collections, etc
 *
 * @param <T> Type of encodable
 */
public interface EncodableMixin<T extends Encodable> extends BACnetJsonMixin {

    T unwrap();

}
