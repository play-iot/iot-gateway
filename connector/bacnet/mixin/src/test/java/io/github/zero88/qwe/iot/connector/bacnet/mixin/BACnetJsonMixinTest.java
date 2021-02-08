package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

class BACnetJsonMixinTest {

    @Test
    void serialize_priority_value() {
        Assertions.assertEquals("present-value", PropertyIdentifier.presentValue.toString());
        Assertions.assertEquals("present-value",
                                BACnetJsonMixin.standardizeKey(PropertyIdentifier.presentValue.toString()));
    }

}
