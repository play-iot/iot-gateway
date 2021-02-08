package io.github.zero88.qwe.iot.data.unit;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.AngularVelocity;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Base;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.ElectricPotential;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Illumination;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Power;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Pressure;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Temperature;
import io.vertx.core.json.JsonObject;

public class DataTypeTest {

    @Test
    public void test_serialize_dataType() throws JSONException {
        JsonHelper.assertJson(new JsonObject("{\"type\":\"number\", \"category\":\"ALL\"}"), Base.NUMBER.toJson());
        JsonHelper.assertJson(new JsonObject("{\"type\":\"percentage\", \"symbol\": \"%\", \"category\":\"ALL\"}"),
                              Base.PERCENTAGE.toJson());
        JsonHelper.assertJson(
            new JsonObject("{\"type\":\"celsius\", \"symbol\": \"°C\", \"category\":\"TEMPERATURE\"}"),
            Temperature.CELSIUS.toJson());
        JsonHelper.assertJson(
            new JsonObject("{\"type\":\"volt\", \"symbol\": \"V\", \"category\":\"ELECTRIC_POTENTIAL\"}"),
            ElectricPotential.VOLTAGE.toJson());
        JsonHelper.assertJson(new JsonObject("{\"type\":\"dBm\", \"symbol\": \"dBm\", \"category\":\"POWER\"}"),
                              Power.DBM.toJson());
        JsonHelper.assertJson(
            new JsonObject("{\"type\":\"hectopascal\", \"symbol\": \"hPa\", \"category\":\"PRESSURE\"}"),
            Pressure.HPA.toJson());
        JsonHelper.assertJson(new JsonObject("{\"type\":\"lux\", \"symbol\": \"lx\", \"category\":\"ILLUMINATION\"}"),
                              Illumination.LUX.toJson());
        JsonHelper.assertJson(
            new JsonObject("{\"type\":\"kilowatt_hour\", \"symbol\": \"kWh\", \"category\":\"POWER\"}"),
            Power.KWH.toJson());
        JsonHelper.assertJson(new JsonObject(
                                  "{\"type\":\"revolutions_per_minute\", \"symbol\": \"rpm\", " + "\"category" +
                                  "\":\"ANGULAR_VELOCITY\"}"),
                              AngularVelocity.RPM.toJson());
        JsonHelper.assertJson(new JsonObject("{\"type\":\"bool\",\"category\":\"ALL\"}"), Base.BOOLEAN.toJson());
    }

    @Test
    public void test_deserialize_numberType() {
        assertNumberDataType("{\"type\":\"number\"}", "number", null);
        assertNumberDataType("{\"type\":\"percentage\"}", "percentage", "%");
        assertNumberDataType("{\"type\":\"celsius\"}", "celsius", "°C");
        assertNumberDataType("{\"type\":\"volt\"}", "volt", "V");
        assertNumberDataType("{\"type\":\"dBm\"}", "dBm", "dBm");
        assertNumberDataType("{\"type\":\"hectopascal\"}", "hectopascal", "hPa");
        assertNumberDataType("{\"type\":\"lux\"}", "lux", "lx");
        assertNumberDataType("{\"type\":\"kilowatt_hour\"}", "kilowatt_hour", "kWh");
        assertNumberDataType("{\"type\":\"unknown\", \"symbol\": \"xxx\"}", "unknown", "xxx");
    }

    private void assertNumberDataType(String from, String type, String unit) {
        final DataType dt = JsonData.from(from, DataType.class);
        Assert.assertTrue(dt instanceof NumberDataType);
        Assert.assertEquals(type, dt.type());
        Assert.assertEquals(unit, dt.unit());
        Assert.assertNull(dt.alias());
    }

    @Test
    public void test_deserialize_booleanType() {
        final DataType dt = JsonData.from("{\"type\":\"bool\"}", DataType.class);
        Assert.assertTrue(dt instanceof BooleanDataType);
        Assert.assertEquals("bool", dt.type());
        Assert.assertNull(dt.unit());
        Assert.assertEquals(1d, dt.parse("true"), 0.0);
        Assert.assertEquals(1d, dt.parse(10), 0.0);
        Assert.assertEquals(1d, dt.parse(0.5), 0.0);
        Assert.assertEquals(0d, dt.parse(false), 0.0);
        Assert.assertEquals("FALSE", dt.display(dt.parse(false)));
    }

    @Test
    public void test_deserialize_with_display() {
        final UnitAlias alias = new UnitAlias().add(">0", "ON").add("<=0", "OFF");
        final DataType dt = JsonData.from("{\"type\":\"bool\", \"alias\":" + alias.toJson().encode() + "}",
                                          DataType.class);
        Assert.assertTrue(dt instanceof BooleanDataType);
        Assert.assertEquals("bool", dt.type());
        Assert.assertNull(dt.unit());
        Assert.assertEquals("ON", dt.display(dt.parse(1)));
        Assert.assertEquals("ON", dt.display(dt.parse(0.5)));
        Assert.assertEquals("OFF", dt.display(dt.parse(0)));
    }

}
