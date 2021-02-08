package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

class PropertyValueSerializerTest {

    @Test
    public void test_serialize() {
        final PropertyValue value = new PropertyValue(PropertyIdentifier.presentValue, BinaryPV.active);
        final JsonObject json = BACnetJsonMixin.MAPPER.convertValue(value, JsonObject.class);
        Assertions.assertEquals(new JsonObject("{\"arrayIndex\":null,\"priority\":null,\"value\":{\"rawValue\":1," +
                                               "\"value\":\"active\"},\"propertyIdentifier\":\"present-value\"}"),
                                json);
    }

    @Test
    public void test_serialize_in_sequence() {
        SequenceOf<PropertyValue> values = new SequenceOf<>(
            new PropertyValue(PropertyIdentifier.presentValue, new UnsignedInteger(1), BinaryPV.inactive,
                              new UnsignedInteger(2)),
            new PropertyValue(PropertyIdentifier.presentValue, Boolean.TRUE));
        final JsonArray array = BACnetJsonMixin.MAPPER.convertValue(values, JsonArray.class);
        Assertions.assertEquals(new JsonArray("[{\"arrayIndex\":1,\"priority\":2,\"value\":{\"rawValue\":0," +
                                              "\"value\":\"inactive\"},\"propertyIdentifier\":\"present-value\"}," +
                                              "{\"arrayIndex\":null,\"priority\":null,\"value\":true," +
                                              "\"propertyIdentifier\":\"present-value\"}]"), array);
    }

}
