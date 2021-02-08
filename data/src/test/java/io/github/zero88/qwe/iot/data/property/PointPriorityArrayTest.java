package io.github.zero88.qwe.iot.data.property;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.exceptions.CarlException;
import io.vertx.core.json.JsonObject;

public class PointPriorityArrayTest {

    private PointPriorityArray ppa;

    @Before
    public void setup() {
        ppa = new PointPriorityArray().add(8).add(9, 9.0).add(2, 3.5).add(10, 10.5);
    }

    @Test
    public void test_serialize() throws JSONException {
        JsonHelper.assertJson(new JsonObject("{\"1\":null,\"2\":3.5,\"3\":null,\"4\":null,\"5\":null,\"6\":null," +
                                             "\"7\":null,\"8\":null,\"9\":9.0,\"10\":10.5,\"11\":null,\"12\":null," +
                                             "\"13\":null,\"14\":null,\"15\":null,\"16\":8.0,\"17\":null}"),
                              ppa.toJson());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_add_invalid() {
        ppa.add(18, 4);
    }

    @Test
    public void test_deserialize() throws JSONException {
        PointPriorityArray from = JsonData.from("{\"2\":3.5,\"16\":8.0,\"9\":9.0,\"10\":10.5}",
                                                PointPriorityArray.class);
        JsonHelper.assertJson(new JsonObject("{\"1\":null,\"2\":3.5,\"3\":null,\"4\":null,\"5\":null,\"6\":null," +
                                             "\"7\":null,\"8\":null,\"9\":9.0,\"10\":10.5,\"11\":null,\"12\":null," +
                                             "\"13\":null,\"14\":null,\"15\":null,\"16\":8.0,\"17\":null}"),
                              from.toJson());
        Assert.assertEquals(ppa, from);
        Double aDouble = from.get(9);
        Double expected = 9.0d;
        Assert.assertEquals(aDouble, expected);
    }

    @Test
    public void test_get_highest_value() {
        final PointPresentValue highestValue = ppa.findHighestValue();
        Assert.assertEquals(2, highestValue.getPriority());
        Assert.assertEquals(3.5d, highestValue.getPointValue().getRawValue(), 0.0);
    }

    @Test
    public void test_get_do_not_add_value_when_data_is_null() throws JSONException {
        final PointPriorityArray pointPriorityArray = ppa.add(1, null);
        JsonHelper.assertJson(new JsonObject("{\"1\":null,\"2\":3.5,\"3\":null,\"4\":null,\"5\":null,\"6\":null," +
                                             "\"7\":null,\"8\":null,\"9\":9.0,\"10\":10.5,\"11\":null,\"12\":null," +
                                             "\"13\":null,\"14\":null,\"15\":null,\"16\":8.0,\"17\":null}"),
                              pointPriorityArray.toJson());
    }

    @Test
    public void test_get_next_highest_value_with_highest_is_null() {
        final PointPresentValue highestValue = ppa.add(1, null).findHighestValue();
        Assert.assertEquals(2, highestValue.getPriority());
        Assert.assertEquals(3.5d, highestValue.getPointValue().getRawValue(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deserialize_invalid_priority() throws Throwable {
        try {
            JsonData.from(new JsonObject("{\"90\":3.5}"), PointPriorityArray.class);
        } catch (CarlException e) {
            final Throwable rootCause = e.getCause().getCause().getCause().getCause();
            Assert.assertEquals("Priority is only in range [1, 17]", rootCause.getMessage());
            throw rootCause;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_deserialize_invalid_value() throws Throwable {
        try {
            JsonData.from(new JsonObject("{\"12\":\"444.5s\"}"), PointPriorityArray.class);
        } catch (CarlException e) {
            final Throwable rootCause = e.getCause().getCause().getCause().getCause();
            Assert.assertEquals("Value must be number", rootCause.getMessage());
            throw rootCause;
        }
    }

    @Test
    public void test_merge() throws Throwable {
        final PointPriorityArray merge = JsonData.merge(ppa.toJson(), new JsonObject(
            "{\"9\":55,\"10\":14, \"11\":null,\"12\":\"444.5\"}"), PointPriorityArray.class);
        JsonHelper.assertJson(new JsonObject("{\"1\":null,\"2\":3.5,\"3\":null,\"4\":null,\"5\":null,\"6\":null," +
                                             "\"7\":null,\"8\":null,\"9\":55.0,\"10\":14.0,\"11\":null,\"12\":444.5," +
                                             "\"13\":null,\"14\":null,\"15\":null,\"16\":8.0,\"17\":null}"),
                              merge.toJson());
    }

}
