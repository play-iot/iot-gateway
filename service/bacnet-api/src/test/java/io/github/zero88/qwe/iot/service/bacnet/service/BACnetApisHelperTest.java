package io.github.zero88.qwe.iot.service.bacnet.service;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class BACnetApisHelperTest {

    @Test
    public void test_count_api() {
        final Set<? extends BACnetApis> services = BACnetApisHelper.createServices(
            SharedDataLocalProxy.create(Vertx.vertx(), BACnetApisHelper.class.getName()));
        Assert.assertEquals(8, services.size());
    }

}
