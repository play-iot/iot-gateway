package io.github.zero88.qwe.iot.connector.bacnet.discovery;

import org.json.JSONException;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.protocol.network.Ipv4Network;
import io.vertx.core.json.JsonObject;

public class DiscoveryParamsTest {

    @Test
    public void test_serialize() throws JSONException {
        final Ipv4Network firstActiveIp = Ipv4Network.getFirstActiveIp();
        final DiscoveryParams params = DiscoveryParams.builder()
                                                      .networkId(firstActiveIp.identifier())
                                                      .deviceInstance(1)
                                                      .objectCode("analog-value:2")
                                                      .build();
        final JsonObject expected = new JsonObject().put("networkId", firstActiveIp.identifier())
                                                    .put("deviceInstance", 1)
                                                    .put("objectCode", "analog-value:2");
        System.out.println(params.toJson());
        JsonHelper.assertJson(expected, params.toJson());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_validate_network() {
        final JsonObject params = new JsonObject().put("networkId", "");
        DiscoveryParams.from(params, DiscoveryLevel.NETWORK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_validate_device_missing_network() {
        final JsonObject params = new JsonObject().put("networkId", "").put("deviceInstance", 1);
        DiscoveryParams.from(params, DiscoveryLevel.DEVICE);
    }

    @Test(expected = NullPointerException.class)
    public void test_validate_device_missing_device() {
        final JsonObject params = new JsonObject().put("networkId", "xyz");
        DiscoveryParams.from(params, DiscoveryLevel.DEVICE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_validate_object_missing_network() {
        final JsonObject params = new JsonObject().put("deviceInstance", 1).put("objectCode", "1");
        DiscoveryParams.from(params, DiscoveryLevel.OBJECT);
    }

    @Test(expected = NullPointerException.class)
    public void test_validate_object_missing_device() {
        final JsonObject params = new JsonObject().put("networkId", "xyz").put("objectCode", "1");
        DiscoveryParams.from(params, DiscoveryLevel.OBJECT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_validate_object_missing_obj() {
        final JsonObject params = new JsonObject().put("networkId", "xyz").put("deviceInstance", 1);
        DiscoveryParams.from(params, DiscoveryLevel.OBJECT);
    }

    @Test
    public void test_deserialize_full() throws JSONException {
        final JsonObject body = new JsonObject().put("networkId", "xx")
                                                .put("deviceInstance", 1)
                                                .put("objectCode", "analog-value:2");
        final DiscoveryParams params = DiscoveryParams.from(body, DiscoveryLevel.OBJECT);
        JsonHelper.assertJson(body, params.toJson());
    }

}
