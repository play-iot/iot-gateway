package io.github.zero88.qwe.iot.connector.bacnet.simulator;

import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.utils.Configs;

public class SimulatorConfigTest {

    @Test
    public void deserialize() {
        SimulatorConfig fromFile = IConfig.from(Configs.loadJsonConfig("bacnetTestConfig.json"), SimulatorConfig.class);
        Assert.assertNotNull(fromFile);
        Assert.assertEquals("QWEIOEdge28TEST", fromFile.getDeviceName());
        Assert.assertEquals(654321, fromFile.getDeviceId());

        Assert.assertNotNull(fromFile.getNetworks());
        Assert.assertEquals(2, fromFile.getNetworks().size());
        fromFile.getNetworks().toNetworks().forEach(ipConfig -> {
            Assert.assertNotNull(ipConfig);
            Assert.assertFalse(ipConfig.label().isEmpty());
        });
    }

}
