package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.CharacterString;

class AccessResultMixinTest {

    @Test
    public void test_serialize() {
        final Result r1 = new Result(PropertyIdentifier.description, null, new CharacterString("mm"));
        final Result r2 = new Result(PropertyIdentifier.accessDoors, null,
                                     new ErrorClassAndCode(ErrorClass.property, ErrorCode.unknownProperty));
        final Result r3 = new Result(PropertyIdentifier.presentValue, null, BinaryPV.active);
        final Result r4 = new Result(PropertyIdentifier.priorityArray, null, new PriorityArray());
        final AccessResultMixin mixin = AccessResultMixin.create(r1, r2, r3, r4);
        final JsonObject expected = new JsonObject(
            "{\"present-value\":{\"rawValue\":1,\"value\":\"active\"},\"access-doors\":{\"errorClass\":\"property\"," +
            "\"errorCode\":\"unknown-property\"},\"priority-array\":{\"1\":null,\"2\":null,\"3\":null,\"4\":null," +
            "\"5\":null,\"6\":null,\"7\":null,\"8\":null,\"9\":null,\"10\":null,\"11\":null,\"12\":null,\"13\":null," +
            "\"14\":null,\"15\":null,\"16\":null},\"description\":\"mm\"}");
        Assertions.assertEquals(expected, mixin.toJson());
    }

}
