package io.github.zero88.qwe.iot.data.property;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.property.PointPresentValue.Fields;
import io.vertx.core.json.JsonObject;

public class PointPresentValueTest {

    @Test
    public void test_serialize() throws JSONException {
        final JsonObject e = new JsonObject().put(Fields.priority, 1)
                                             .put(PointValue.Fields.value, "0")
                                             .put(PointValue.Fields.rawValue, 0);
        System.out.println(e);
        JsonHelper.assertJson(e, PointPresentValue.builder().priority(1).value("0").build().toJson());
    }

    @Test
    public void test_deserialize_no_priority() {
        final PointPresentValue ppv = JsonData.from("{\"value\":\"1\"}", PointPresentValue.class);
        Assert.assertEquals(16, ppv.getPriority());
        Assert.assertEquals("1", ppv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(1.0), ppv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_no_raw() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"value\":\"1\"}", PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("1", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(1.0), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_no_value() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"rawValue\": 2.0}", PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("2.0", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(2.0), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_value_is_raw() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"value\":10}", PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("10", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(10.0), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_full() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"value\":\"3\",\"rawValue\":3}",
                                                   PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("3", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(3.0), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_only_value_is_string() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"value\":\"active\"}", PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("active", pv.getPointValue().getValue());
        Assert.assertNull(pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_value_is_string_and_raw_has_value() {
        final PointPresentValue pv = JsonData.from("{\"priority\":1,\"value\":\"on\",\"rawValue\":4}",
                                                   PointPresentValue.class);
        Assert.assertEquals(1, pv.getPriority());
        Assert.assertEquals("on", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(4), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_deserialize_from_invalid_json() {
        JsonObject json = new JsonObject().put("a", 1).put("b", 2);
        Assert.assertNull(PointPresentValue.from(json));
    }

    @Test
    public void test_deserialize_from_json() {
        JsonObject json = new JsonObject().put("priority", 10).put("value", 2);
        PointPresentValue pv = PointPresentValue.from(json);
        Assert.assertNotNull(pv);
        Assert.assertEquals(10, pv.getPriority());
        Assert.assertEquals("2", pv.getPointValue().getValue());
        Assert.assertEquals(Double.valueOf(2), pv.getPointValue().getRawValue());
    }

    @Test
    public void test_create_def() {
        PointPresentValue pv = PointPresentValue.def();
        Assert.assertNotNull(pv);
        Assert.assertEquals(16, pv.getPriority());
        Assert.assertNull(pv.getPointValue().getValue());
        Assert.assertNull(pv.getPointValue().getRawValue());
    }

}
