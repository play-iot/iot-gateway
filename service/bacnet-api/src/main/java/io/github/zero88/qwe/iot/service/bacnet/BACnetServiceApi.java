package io.github.zero88.qwe.iot.service.bacnet;

import java.util.Objects;
import java.util.Optional;

import io.github.zero88.qwe.component.ContextLookup;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.http.event.WebSocketServerEventMetadata;
import io.github.zero88.qwe.http.server.HttpServerProvider;
import io.github.zero88.qwe.http.server.HttpServerRouter;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetApplication;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.connector.bacnet.internal.listener.WhoIsListener;
import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetCacheInitializer;
import io.github.zero88.qwe.iot.service.bacnet.service.BACnetApisHelper;
import io.github.zero88.qwe.iot.service.bacnet.service.coordinator.CoordinatorHelper;
import io.github.zero88.qwe.iot.service.bacnet.service.scheduler.BACnetSchedulerApis;
import io.github.zero88.qwe.iot.service.bacnet.websocket.WebSocketCOVSubscriber;
import io.github.zero88.qwe.micro.MicroContext;
import io.github.zero88.qwe.micro.MicroVerticleProvider;
import io.github.zero88.qwe.micro.ServiceDiscoveryInvoker;
import io.github.zero88.qwe.scheduler.SchedulerProvider;
import io.github.zero88.qwe.storage.json.JsonStorageProvider;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;


import lombok.NonNull;

/*
 * BACnet Application
 */
public class BACnetServiceApi extends BACnetApplication<BACnetServiceConfig> {

    @Override
    public String configFile() {
        return "bacnet.json";
    }

    @Override
    public void start() {
        super.start();
        this.addProvider(new HttpServerProvider(initRouter()))
            .addProvider(new MicroVerticleProvider())
            .addProvider(new SchedulerProvider())
            .addProvider(new JsonStorageProvider());
    }

    @Override
    protected @NonNull Class<BACnetServiceConfig> bacnetConfigClass() {
        return BACnetServiceConfig.class;
    }

    @Override
    protected Single<JsonObject> initialize(@NonNull ContextLookup contextLookup, @NonNull BACnetServiceConfig config) {
        final ServiceDiscoveryInvoker invoker = contextLookup.query(MicroContext.class).getLocalInvoker();
        new BACnetCacheInitializer().init(this);
        return this.registerBACnetApis(invoker, sharedData(), config)
                   .doOnSuccess(total -> logger.debug("Registered " + total + " BACnet APIs"))
                   .flatMap(json -> BACnetSchedulerApis.registerApis(invoker, sharedData())
                                                       .map(Record::toJson)
                                                       .doOnSuccess(o -> logger.debug("Registered 1 Scheduler APIs")))
                   .flatMap(ignore -> new CoordinatorHelper().restartBACnetCovCoordinator(sharedData()));
    }

    @NonNull
    protected Single<Long> registerBACnetApis(@NonNull ServiceDiscoveryInvoker invoker,
                                              @NonNull SharedDataLocalProxy sharedData,
                                              @NonNull BACnetServiceConfig config) {
        return Observable.fromIterable(BACnetApisHelper.createServices(sharedData))
                         .doOnEach(so -> Optional.ofNullable(so.getValue())
                                                 .ifPresent(s -> getEventbus().register(s.address(), s)))
                         .filter(s -> Objects.nonNull(s.definitions()))
                         .flatMap(s -> Observable.fromIterable(s.definitions())
                                                 .flatMapSingle(
                                                     d -> invoker.addEventMessageRecord(s.api(), s.address(), d)))
                         .count();
    }

    @Override
    protected void addListenerOnEachDevice(@NonNull BACnetDevice device) {
        device.addListeners(new WhoIsListener());
    }

    private HttpServerRouter initRouter() {
        final WebSocketCOVSubscriber subscriber = WebSocketCOVSubscriber.builder().build();
        return new HttpServerRouter().registerEventBusSocket(
            WebSocketServerEventMetadata.create(subscriber.getWsPath(), subscriber.toEventModel()));
    }

}
