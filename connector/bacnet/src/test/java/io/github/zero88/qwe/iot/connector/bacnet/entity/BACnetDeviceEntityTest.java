package io.github.zero88.qwe.iot.connector.bacnet.entity;

import org.json.JSONException;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.RemoteDeviceMixin;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyValues;

public class BACnetDeviceEntityTest {

    @Test
    public void test_serialize() throws JSONException {
        final String networkId = "ipv4-docker";
        final ObjectIdentifier deviceId = new ObjectIdentifier(ObjectType.device, 111);
        final PropertyValues pvs = new PropertyValues();
        pvs.add(deviceId, PropertyIdentifier.objectName, null, new CharacterString("abc"));
        pvs.add(deviceId, PropertyIdentifier.systemStatus, null, DeviceStatus.operational);
        pvs.add(deviceId, PropertyIdentifier.vendorName, null, new CharacterString("xz"));
        final Address address = new Address(new byte[] {(byte) 206, (byte) 210, 100, (byte) 134});
        final RemoteDeviceMixin mixin = RemoteDeviceMixin.create(deviceId, address, pvs);
        final BACnetDeviceEntity dt = BACnetDeviceEntity.from(networkId, mixin);
        final JsonObject expected = new JsonObject(
            "{\"_key\":\"device:111\",\"_networkId\":\"ipv4-docker\",\"_address\":{\"type\":\"IP\"," +
            "\"networkNumber\":0,\"hostAddress\":\"206.210.100.134\",\"macAddress\":\"CE-D2-64-86\"}," +
            "\"_type\":\"MACHINE\",\"_name\":\"abc\",\"_status\":\"UP\",\"name\":\"abc\"," +
            "\"address\":{\"type\":\"IP\",\"networkNumber\":0,\"hostAddress\":\"206.210.100.134\"," +
            "\"macAddress\":\"CE-D2-64-86\"},\"instanceNumber\":111,\"system-status\":{\"rawValue\":0," +
            "\"value\":\"operational\"},\"vendor-name\":\"xz\",\"object-name\":\"abc\"}");
        JsonHelper.assertJson(expected, dt.toJson());
    }

}
