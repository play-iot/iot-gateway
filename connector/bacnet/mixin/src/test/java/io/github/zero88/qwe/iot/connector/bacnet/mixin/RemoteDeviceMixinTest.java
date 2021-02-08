package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import io.github.zero88.qwe.JsonHelper;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Double;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyValues;

public class RemoteDeviceMixinTest {

    private ObjectIdentifier oid;
    private PropertyValues pvs;

    @Before
    public void setup() {
        oid = new ObjectIdentifier(ObjectType.device, 222);
        pvs = new PropertyValues();
        pvs.add(oid, PropertyIdentifier.groupId, null, new CharacterString("xxx"));
        pvs.add(oid, PropertyIdentifier.alarmValue, null, new Double(10.0));
    }

    @Test
    public void test_serialize_ip_mode() throws JSONException {
        final Address address = new Address(new byte[] {(byte) 206, (byte) 210, 100, (byte) 134});
        pvs.add(oid, PropertyIdentifier.objectName, null, new CharacterString("test"));
        final RemoteDeviceMixin rdm = RemoteDeviceMixin.create(oid, address, pvs);
        final JsonObject expected = new JsonObject(
            "{\"instanceNumber\":222,\"name\":\"test\",\"address\":{\"type\":\"IP\"," +
            "\"networkNumber\":0,\"hostAddress\":\"206.210.100.134\"," +
            "\"macAddress\":\"CE-D2-64-86\"},\"group-id\":\"xxx\",\"object-name\":\"test\",\"alarm-value\":10.0}");
        JsonHelper.assertJson(expected, rdm.toJson());
    }

    @Test
    public void test_serialize_mstp_mode() throws JSONException {
        final Address address = new Address(3, new byte[] {(byte) 12});
        pvs.add(oid, PropertyIdentifier.objectName, null, new CharacterString("hello"));
        final RemoteDeviceMixin rdm = RemoteDeviceMixin.create(oid, address, pvs);
        final JsonObject expected = new JsonObject(
            "{\"instanceNumber\":222,\"name\":\"hello\",\"address\":{\"type\":\"MSTP\",\"networkNumber\":3," +
            "\"hostAddress\":\"12\",\"macAddress\":\"0C\"},\"group-id\":\"xxx\",\"object-name\":\"hello\"," +
            "\"alarm-value\":10.0}");
        JsonHelper.assertJson(expected, rdm.toJson());
    }

}
