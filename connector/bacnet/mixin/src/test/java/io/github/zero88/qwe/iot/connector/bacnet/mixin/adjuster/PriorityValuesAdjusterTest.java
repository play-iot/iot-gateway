package io.github.zero88.qwe.iot.connector.bacnet.mixin.adjuster;

import org.junit.Assert;
import org.junit.Test;

import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.enumerated.AbortReason;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class PriorityValuesAdjusterTest {

    @Test
    public void test_adjust_unmatched_value() {
        final PriorityArray array = new PriorityArray();
        array.put(2, new UnsignedInteger(100));
        final PriorityArray values = new PriorityValuesAdjuster().apply(Boolean.TRUE, array);
        final PriorityValue base1 = values.getBase1(1);
        Assert.assertNotNull(base1);
        Assert.assertEquals(Null.instance, base1.getConstructedValue());
        final PriorityValue base2 = values.getBase1(2);
        Assert.assertNotNull(base2);
        Assert.assertEquals(new UnsignedInteger(100), base2.getConstructedValue());
    }

    @Test
    public void test_adjust_matched_value() {
        final PriorityArray array = new PriorityArray();
        array.put(1, new CharacterString(AbortReason.outOfResources.toString()));
        final PriorityArray values = new PriorityValuesAdjuster().apply(AbortReason.other, array);
        final PriorityValue base1 = values.getBase1(1);
        Assert.assertNotNull(base1);
        Assert.assertEquals(AbortReason.outOfResources, base1.getConstructedValue());
    }

}
