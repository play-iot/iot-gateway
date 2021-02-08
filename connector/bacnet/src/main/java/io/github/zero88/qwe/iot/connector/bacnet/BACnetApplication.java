package io.github.zero88.qwe.iot.connector.bacnet;

import java.util.Objects;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.component.ApplicationVerticle;
import io.github.zero88.qwe.component.ContextLookup;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.iot.connector.bacnet.handler.DiscoverCompletionHandler;
import io.github.zero88.qwe.utils.ExecutorHelpers;
import io.reactivex.Single;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.event.DeviceEventListener;

import lombok.NonNull;

public abstract class BACnetApplication<C extends BACnetConfig> extends ApplicationVerticle {

    @Override
    public void start() {
        super.start();
        C bacnetConfig = IConfig.from(this.config.getAppConfig(), bacnetConfigClass());
        if (logger.isDebugEnabled()) {
            logger.debug("BACnet application configuration: {}", bacnetConfig.toJson());
        }
        this.addData(BACnetDevice.CONFIG_KEY, bacnetConfig);
    }

    @Override
    public void stop(Promise<Void> future) {
        shutdown().doOnSuccess(result -> logger.info(result.encode()))
                  .subscribe(ignore -> super.stop(future), future::fail);
    }

    @Override
    public void onInstallCompleted(@NonNull ContextLookup lookup) {
        C bacnetConfig = sharedData().getData(BACnetDevice.CONFIG_KEY);
        this.getEventbus().register(bacnetConfig.getCompleteDiscoverAddress(), createDiscoverCompletionHandler());
        ExecutorHelpers.blocking(getVertx(), initialize(lookup, bacnetConfig))
                       .doOnSuccess(o -> logger.info("Initialize BACnet successfully. Result: {}", o))
                       .subscribe((d, e) -> readinessHandler(bacnetConfig, d, e));
    }

    /**
     * Register BACnet config class to parsing data
     *
     * @return BACnet config class
     */
    @NonNull
    protected abstract Class<C> bacnetConfigClass();

    /**
     * Add one or more {@code BACnet listeners} after each {@code BACnet device} on each network starts
     *
     * @param device BACnet device
     * @see BACnetDevice
     * @see DeviceEventListener
     */
    protected abstract void addListenerOnEachDevice(@NonNull BACnetDevice device);

    @NonNull
    protected DiscoverCompletionHandler createDiscoverCompletionHandler() {
        return new DiscoverCompletionHandler();
    }

    /**
     * Initialize BACnet application
     *
     * @param contextLookup context lookup
     * @param config        BACnet config
     * @return a initialization result
     */
    protected Single<JsonObject> initialize(@NonNull ContextLookup contextLookup, @NonNull C config) {
        return Single.just(new JsonObject().put("message", "No initialize service"));
    }

    protected void readinessHandler(@NonNull C config, JsonObject d, Throwable e) {
        final EventMessage msg = Objects.nonNull(e)
                                 ? EventMessage.error(EventAction.NOTIFY_ERROR, e)
                                 : EventMessage.initial(EventAction.NOTIFY, RequestData.builder().body(d).build());
        getEventbus().publish(config.getReadinessAddress(), msg);
    }

    /**
     * Shutdown BACnet application
     *
     * @return a shutdown result
     */
    protected Single<JsonObject> shutdown() {
        return Single.just(new JsonObject().put("message", "Shut down complete"));
    }

}
