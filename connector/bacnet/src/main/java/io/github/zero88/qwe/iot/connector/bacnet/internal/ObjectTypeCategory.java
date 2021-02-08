package io.github.zero88.qwe.iot.connector.bacnet.internal;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;

public final class ObjectTypeCategory {

    public static boolean isPoint(@NonNull ObjectIdentifier objectId) {
        return isPoint(objectId.getObjectType());
    }

    public static boolean isPoint(@NonNull ObjectType objectType) {
        return objectType.isOneOf(ObjectType.analogInput, ObjectType.analogOutput, ObjectType.analogValue,
                                  ObjectType.binaryInput, ObjectType.binaryOutput, ObjectType.binaryValue,
                                  ObjectType.multiStateInput, ObjectType.multiStateOutput, ObjectType.multiStateValue,
                                  ObjectType.accessDoor, ObjectType.largeAnalogValue, ObjectType.lightingOutput,
                                  ObjectType.binaryLightingOutput);
    }

}
