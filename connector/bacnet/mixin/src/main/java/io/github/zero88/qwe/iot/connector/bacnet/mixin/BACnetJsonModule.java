package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer.EncodableSerializer;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.PropertyValuesMixin.PropertyValuesSerializer;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer.ObjectIdentifierKeySerializer;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer.PropertyIdentifierKeySerializer;
import io.github.zero88.utils.Reflections.ReflectionClass;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

final class BACnetJsonModule {

    static final SimpleModule MODULE;

    static {
        MODULE = new SimpleModule();
        MODULE.addKeySerializer(ObjectIdentifier.class, new ObjectIdentifierKeySerializer());
        MODULE.addKeySerializer(PropertyIdentifier.class, new PropertyIdentifierKeySerializer());
        ReflectionClass.stream(BACnetJsonModule.class.getPackage().getName(), EncodableSerializer.class,
                               ReflectionClass.publicClass())
                       .map(ReflectionClass::createObject)
                       .forEach(MODULE::addSerializer);
        MODULE.addSerializer(Encodable.class, EncodableSerializer.DEFAULT);
        MODULE.addSerializer(PropertyValuesMixin.class, new PropertyValuesSerializer());
    }
}
