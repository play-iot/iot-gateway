package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import org.junit.Assert;
import org.junit.Test;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryLightingPV;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class EncodableDeserializerTest {

    @Test
    public void test_analog_input() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.analogInput, 1),
                                                            PropertyIdentifier.presentValue, "10");
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof Real);
        Assert.assertEquals(10f, ((Real) value).floatValue(), 0);
    }

    @Test
    public void test_analog_output() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.analogOutput, 1),
                                                            PropertyIdentifier.presentValue, 15.2);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof Real);
        Assert.assertEquals(15.2f, ((Real) value).floatValue(), 0);
    }

    @Test
    public void test_analog_value() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.analogValue, 1),
                                                            PropertyIdentifier.presentValue, 23.5);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof Real);
        Assert.assertEquals(23.5f, ((Real) value).floatValue(), 0);
    }

    @Test
    public void test_binary_input_by_id() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryInput, 1),
                                                            PropertyIdentifier.presentValue, 0);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryPV);
        Assert.assertEquals(BinaryPV.inactive, value);
    }

    @Test
    public void test_binary_input_by_name() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryInput, 1),
                                                            PropertyIdentifier.presentValue, "active");
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryPV);
        Assert.assertEquals(BinaryPV.active, value);
    }

    @Test
    public void test_binary_output_by_id() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryOutput, 1),
                                                            PropertyIdentifier.presentValue, 0);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryPV);
        Assert.assertEquals(BinaryPV.inactive, value);
    }

    @Test
    public void test_binary_value() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryValue, 1),
                                                            PropertyIdentifier.presentValue, "active");
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryPV);
        Assert.assertEquals(BinaryPV.active, value);
    }

    @Test
    public void test_binary_lightningOutput_by_name() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryLightingOutput, 1),
                                                            PropertyIdentifier.presentValue, "on");
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryLightingPV);
        Assert.assertEquals(BinaryLightingPV.on, value);
    }

    @Test
    public void test_binary_lightningOutput_by_id() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.binaryLightingOutput, 1),
                                                            PropertyIdentifier.presentValue, 5);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof BinaryLightingPV);
        Assert.assertEquals(BinaryLightingPV.stop, value);
    }

    @Test
    public void test_multipleState_input() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.multiStateInput, 1),
                                                            PropertyIdentifier.presentValue, 23.5);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof UnsignedInteger);
        Assert.assertEquals(23, ((UnsignedInteger) value).intValue());
    }

    @Test
    public void test_multipleState_output() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.multiStateOutput, 1),
                                                            PropertyIdentifier.presentValue, 10);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof UnsignedInteger);
        Assert.assertEquals(10, ((UnsignedInteger) value).intValue());
    }

    @Test
    public void test_multipleState_value() {
        final Encodable value = EncodableDeserializer.parse(new ObjectIdentifier(ObjectType.multiStateValue, 1),
                                                            PropertyIdentifier.presentValue, 200);
        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof UnsignedInteger);
        Assert.assertEquals(200, ((UnsignedInteger) value).intValue());
    }

}
