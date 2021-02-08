package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import org.json.JSONException;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.PropertyValuesMixin;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.type.constructed.LimitEnable;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.enumerated.AbortReason;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class PriorityArraySerializerTest {

    @Test
    public void test_serialize_priority_array() throws JSONException {
        final PriorityArray array = new PriorityArray();
        array.put(1, new PriorityValue(new CharacterString("xxx")));
        array.put(2, new PriorityValue(new UnsignedInteger(10)));
        array.put(3, new PriorityValue(AbortReason.other));
        array.put(4, new PriorityValue(Boolean.TRUE));
        array.put(5, new PriorityValue(new Time(10, 5, 10, 0)));
        array.put(6, new PriorityValue(new Date(2020, Month.JANUARY, 21, DayOfWeek.TUESDAY)));
        array.put(7, new PriorityValue(new LimitEnable(true, false)));
        array.put(8, new PriorityValue(Null.instance));
        array.put(9, new PriorityValue(BinaryPV.active));
        array.put(10, new PriorityValue(ObjectType.device));
        final JsonObject expected = new JsonObject(
            "{\"1\":\"xxx\",\"2\":10,\"3\":{\"rawValue\":0,\"value\":\"other\"},\"4\":true,\"5\":\"10:05:10Z\"," +
            "\"6\":\"2020-01-21Z\",\"7\":{\"low-limit-enable\":true,\"high-limit-enable\":false},\"8\":null," +
            "\"9\":{\"rawValue\":1,\"value\":\"active\"},\"10\":\"device\",\"11\":null,\"12\":null,\"13\":null," +
            "\"14\":null," +
            "\"15\":null,\"16\":null}");
        final JsonObject json = BACnetJsonMixin.MAPPER.convertValue(array, JsonObject.class);
        JsonHelper.assertJson(expected, json);
    }

    @Test
    public void test_deserialize_priority_array() throws JSONException {
        final JsonObject query = new JsonObject("{\"priority-array\":{\"1\":\"xxx\"}}");
        final PropertyValuesMixin mixin = JsonData.convert(query, PropertyValuesMixin.class, BACnetJsonMixin.MAPPER);
        final JsonObject expected = new JsonObject(
            "{\"priority-array\":{\"1\":\"xxx\",\"2\":null,\"3\":null,\"4\":null,\"5\":null,\"6\":null,\"7\":null," +
            "\"8\":null,\"9\":null,\"10\":null,\"11\":null,\"12\":null,\"13\":null,\"14\":null,\"15\":null," +
            "\"16\":null}}");
        JsonHelper.assertJson(expected, mixin.toJson());
    }

}
