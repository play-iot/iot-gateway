package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import io.github.zero88.utils.Functions;

import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;

public interface ObjectIdentifierMixin {

    String SEPARATOR = ":";

    static String serialize(@NonNull ObjectIdentifier objId) {
        return objId.getObjectType().toString() + SEPARATOR + objId.getInstanceNumber();
    }

    static ObjectIdentifier deserialize(@NonNull String id) {
        String[] splitter = id.split(SEPARATOR, 2);
        try {
            final ObjectType objectType = ObjectType.forName(splitter[0]);
            return new ObjectIdentifier(objectType, Functions.getOrThrow(() -> Functions.toInt().apply(splitter[1]),
                                                                         () -> new IllegalArgumentException(
                                                                             "Invalid object identifier format")));
        } catch (BACnetRuntimeException e) {
            throw new IllegalArgumentException("Invalid object identifier format");
        }
    }

}
