package io.github.zero88.qwe.iot.service.bacnet;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.CarlConfig;
import io.github.zero88.qwe.CarlConfig.AppConfig;
import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.VertxHelper;
import io.github.zero88.qwe.component.ReadinessAsserter;
import io.github.zero88.qwe.event.EventbusClient;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.NonNull;

@RunWith(VertxUnitRunner.class)
public abstract class BaseBACnetServiceTest {

    protected Vertx vertx;
    protected EventbusClient eventbus;
    protected String bacnetApplicationId;

    @BeforeClass
    public static void beforeSuite() {
        TestHelper.setup();
        ((Logger) LoggerFactory.getLogger("com.serotonin.bacnet4j")).setLevel(Level.TRACE);
    }

    @Before
    public final void before(TestContext context) {
        this.vertx = Vertx.vertx();
        deployServices(context);
    }

    @After
    public final void after(TestContext context) {
        Async async = context.async();
        this.vertx.undeploy(bacnetApplicationId, event -> {
            TestHelper.testComplete(async);
            this.vertx.close(context.asyncAssertSuccess());
        });
    }

    protected abstract ReadinessAsserter createReadinessHandler(TestContext context, Async async);

    protected abstract void deployServices(TestContext context);

    protected final DeploymentOptions createDeploymentOptions(@NonNull IConfig cfg) {
        final CarlConfig carlConfig = IConfig.from(
            new JsonObject().put(AppConfig.NAME, new JsonObject().put(cfg.key(), cfg.toJson())), CarlConfig.class);
        return new DeploymentOptions().setConfig(carlConfig.toJson());
    }

    protected BACnetServiceConfig createBACnetConfig() {
        return IConfig.fromClasspath("testConfig.json", BACnetServiceConfig.class);
    }

    protected BACnetServiceApi deployBACnetApplication(TestContext context, Async async) {
        final BACnetServiceConfig bacnetCfg = createBACnetConfig();
        final DeploymentOptions options = createDeploymentOptions(bacnetCfg);
        final BACnetServiceApi verticle = new BACnetServiceApi();
        return VertxHelper.deploy(vertx, context, options, verticle, deployId -> {
            bacnetApplicationId = deployId;
            eventbus = verticle.getEventbus()
                               .register(bacnetCfg.getReadinessAddress(), createReadinessHandler(context, async));
        });
    }

}
