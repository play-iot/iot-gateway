package io.github.zero88.qwe.iot.service.bacnet.service.scheduler;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetProtocol;
import io.github.zero88.qwe.iot.connector.rpc.scheduler.SchedulerClient;

import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetCacheInitializer;
import io.github.zero88.qwe.iot.service.bacnet.service.AbstractBACnetRpcClient;

import lombok.NonNull;

public final class BACnetSchedulerClient extends AbstractBACnetRpcClient implements SchedulerClient, BACnetProtocol {

    public BACnetSchedulerClient(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    @Override
    public @NonNull String destination() {
        return sharedData().getData(BACnetCacheInitializer.SCHEDULER_SERVICE_NAME);
    }

}
