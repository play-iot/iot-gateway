package io.github.zero88.qwe.iot.service.bacnet;

import io.github.zero88.qwe.component.ReadinessAsserter;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

public abstract class BACnetWithoutGatewayTest extends BaseBACnetServiceTest {

    protected ReadinessAsserter createReadinessHandler(TestContext context, Async async) {
        return new ReadinessAsserter(context, async, new JsonObject("{\"total\":0}"));
    }

    @Override
    protected void deployServices(TestContext context) {
        deployBACnetApplication(context, context.async());
    }

}
