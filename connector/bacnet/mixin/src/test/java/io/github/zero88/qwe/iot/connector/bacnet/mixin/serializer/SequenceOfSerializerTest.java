package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;

class SequenceOfSerializerTest {

    @Test
    public void test_serialize_priority_array() {
        final SequenceOf<BinaryPV> sequence = new SequenceOf<>(BinaryPV.active, BinaryPV.inactive);
        final JsonArray json = BACnetJsonMixin.MAPPER.convertValue(sequence, JsonArray.class);
        Assertions.assertEquals(new JsonArray().add(new JsonObject("{\"rawValue\":1,\"value\":\"active\"}"))
                                               .add(new JsonObject("{\"rawValue\":0,\"value\":\"inactive\"}")), json);
    }

}
