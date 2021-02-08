package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.Address;

public class AddressMixinTest {

    @Test
    public void test_serialize() throws JSONException {
        final Address address = new Address(new byte[] {(byte) 206, (byte) 210, 100, (byte) 134});
        final JsonObject expected = new JsonObject(
            "{\"type\":\"IP\",\"networkNumber\":0,\"hostAddress\":\"206.210.100.134\"," +
            "\"macAddress\":\"CE-D2-64-86\"}");
        JsonHelper.assertJson(expected, AddressMixin.create(address).toJson());
    }

    @Test
    public void test_deserialize() throws JSONException {
        final JsonObject request = new JsonObject(
            "{\"type\":\"IP\",\"networkNumber\":0,\"hostAddress\":\"206.210.100.134\"," +
            "\"macAddress\":\"CE-D2-64-86\"}");
        AddressMixin mixin = JsonData.convert(request, AddressMixin.class);
        Assert.assertEquals(new Address(new byte[] {(byte) 206, (byte) 210, 100, (byte) 134}), mixin.unwrap());
        JsonHelper.assertJson(request, AddressMixin.create(mixin.unwrap()).toJson());
    }

}
