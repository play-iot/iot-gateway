package io.github.zero88.qwe.iot.service.bacnet;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.component.ApplicationVerticle;
import io.github.zero88.qwe.component.ComponentTestHelper;
import io.github.zero88.qwe.component.ReadinessAsserter;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.micro.MicroConfig;
import io.github.zero88.qwe.micro.MicroContext;
import io.github.zero88.qwe.micro.MicroVerticle;
import io.github.zero88.qwe.micro.MicroVerticleProvider;
import io.github.zero88.qwe.micro.http.EventHttpService;
import io.github.zero88.qwe.micro.register.EventHttpServiceRegister;
import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;

import lombok.NonNull;

public abstract class BACnetWithGatewayTest extends BaseBACnetServiceTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    protected ReadinessAsserter createReadinessHandler(TestContext context, Async async) {
        return new ReadinessAsserter(context, async, new JsonObject("{\"total\":1}"));
    }

    protected void deployServices(TestContext context) {
        eventbus = EventbusClient.create(vertx);
        final Async async = context.async(2);
        deployVerticle(vertx, context, async, () -> deployBACnetApplication(context, async));
    }

    protected MicroConfig getMicroConfig() {
        return IConfig.fromClasspath("mockGateway.json", MicroConfig.class);
    }

    protected void deployVerticle(Vertx vertx, TestContext context, Async async,
                                  Supplier<ApplicationVerticle> supplier) {
        final MicroVerticle v = ComponentTestHelper.deploy(vertx, context, getMicroConfig().toJson(),
                                                           new MicroVerticleProvider(), folder.getRoot().toPath());
        registerMockService(v.getContext()).map(i -> supplier.get())
                                           .subscribe(i -> TestHelper.testComplete(async), context::fail);
    }

    protected abstract Set<EventHttpService> serviceDefinitions();

    private Single<List<Record>> registerMockService(@NonNull MicroContext microContext) {
        return EventHttpServiceRegister.builder()
                                       .vertx(vertx)
                                       .sharedKey(BACnetServiceApi.class.getName())
                                       .eventServices(this::serviceDefinitions)
                                       .build()
                                       .publish(microContext.getLocalInvoker());
    }

}
