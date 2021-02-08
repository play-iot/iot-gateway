package io.github.zero88.qwe.iot.connector.bacnet;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.vertx.core.json.JsonObject;

public class BACnetConfigTest {

    @Test
    public void test_serialize_default() throws JSONException {
        BACnetConfig config = new BACnetConfig();
        JsonHelper.assertJson(new JsonObject("{\"vendorId\":161214,\"vendorName\":\"zero88\",\"deviceId\":89727," +
                                             "\"deviceName\":\"Wanna Fly\",\"modelName\":\"QWE-BACnet\"," +
                                             "\"maxDiscoverTimeout\":10,\"maxDiscoverTimeoutUnit\":\"SECONDS\"," +
                                             "\"completeDiscoverAddress\":\"io.github.zero88.qwe.iot.connector.bacnet" +
                                             ".discover.complete\",\"readinessAddress\":\"io.github.zero88.qwe.iot.connector" +
                                             ".bacnet.readiness\"}"), config.toJson(),
                              JsonHelper.ignore("deviceId"));
    }

    @Test
    public void test_customize() throws JSONException {
        BACnetConfig config = BACnetConfig.builder()
                                          .vendorId(111)
                                          .vendorName("xyz")
                                          .deviceId(123)
                                          .deviceName("wtf")
                                          .modelName("Haha")
                                          .maxDiscoverTimeout(10)
                                          .build();
        BACnetConfig fromFile = IConfig.fromClasspath("test-bacnet-cfg.json", BACnetConfig.class);
        Assert.assertEquals(10, config.getMaxDiscoverTimeout());
        Assert.assertEquals(TimeUnit.SECONDS, config.getMaxDiscoverTimeoutUnit());
        System.out.println(config.toJson());
        JsonHelper.assertJson(config.toJson(), fromFile.toJson());
    }

    @Test
    public void test_deserialize() {
        BACnetConfig config = JsonData.from(
            "{\"vendorId\":1173,\"vendorName\":\"QWE iO Operations Pty Ltd\",\"deviceId\":85084," +
            "\"modelName\":\"QWEIO-Edge28\",\"deviceName\":\"QWEIO-Edge28-85084\",\"maxDiscoverTimeout\":10," +
            "\"maxDiscoverTimeoutUnit\":\"SECONDS\"}", BACnetConfig.class);
        Assert.assertEquals(10000, config.getMaxTimeoutInMS());
        Assert.assertEquals(1173, config.getVendorId());
        Assert.assertEquals("QWE iO Operations Pty Ltd", config.getVendorName());
        Assert.assertEquals("QWEIO-Edge28", config.getModelName());
        Assert.assertEquals(85084, config.getDeviceId());
        Assert.assertEquals("QWEIO-Edge28-85084", config.getDeviceName());
    }

}
