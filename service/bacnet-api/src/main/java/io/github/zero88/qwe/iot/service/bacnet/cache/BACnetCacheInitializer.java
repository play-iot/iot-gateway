package io.github.zero88.qwe.iot.service.bacnet.cache;

import io.github.zero88.qwe.cache.CacheInitializer;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.service.bacnet.BACnetServiceConfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BACnetCacheInitializer implements CacheInitializer {

    public static final String LOCAL_NETWORK_CACHE = "LOCAL_NETWORK_CACHE";
    public static final String BACNET_DEVICE_CACHE = "BACNET_DEVICE_CACHE";
    public static final String BACNET_OBJECT_CACHE = "BACNET_OBJECT_CACHE";
    public static final String SCHEDULER_SERVICE_NAME = "SCHEDULER_SERVICE_NAME";
    public static final String COV_PERSISTENCE_CONFIG = "COV_PERSISTENCE_CONFIG";
    public static final String GATEWAY_ADDRESS = "GATEWAY_ADDRESS";

    @Override
    public void init(@NonNull SharedDataLocalProxy context) {
        BACnetServiceConfig config = context.getData(BACnetDevice.CONFIG_KEY);
        context.addData(GATEWAY_ADDRESS, config.getGatewayAddress());
        context.addData(SCHEDULER_SERVICE_NAME, config.getSchedulerServiceName());
        context.addData(COV_PERSISTENCE_CONFIG, config.getCovCoordinatorPersistence());
        addBlockingCache(context, LOCAL_NETWORK_CACHE, BACnetNetworkCache::init);
        addBlockingCache(context, BACNET_DEVICE_CACHE, () -> BACnetDeviceCache.init(context));
        addBlockingCache(context, BACNET_OBJECT_CACHE, BACnetObjectCache::new);
    }

}
