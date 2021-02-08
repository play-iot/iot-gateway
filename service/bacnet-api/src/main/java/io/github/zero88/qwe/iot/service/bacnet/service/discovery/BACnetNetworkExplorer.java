package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import java.util.Map.Entry;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetNetworkCache;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryOptions;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryParams;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetEntities.BACnetNetworks;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetNetwork;
import io.reactivex.Observable;
import io.reactivex.Single;

import lombok.NonNull;

public final class BACnetNetworkExplorer extends BACnetExplorer<String, BACnetNetwork, BACnetNetworks> {

    BACnetNetworkExplorer(@NonNull SharedDataLocalProxy sharedDataProxy) {
        super(sharedDataProxy);
    }

    @Override
    public Single<BACnetNetwork> discover(@NonNull RequestData reqData) {
        return Single.just(DiscoveryParams.from(reqData, level()))
                     .map(this::parseNetworkProtocol)
                     .map(BACnetNetwork::fromProtocol);
    }

    @Override
    public Single<BACnetNetworks> discoverMany(@NonNull RequestData reqData) {
        final DiscoveryOptions options = parseDiscoverOptions(reqData);
        final BACnetNetworkCache cache = networkCache();
        if (options.isForce()) {
            BACnetNetworkCache.rescan(cache);
        }
        return Observable.fromIterable(cache.all().entrySet())
                         .map(Entry::getValue)
                         .map(BACnetNetwork::fromProtocol)
                         .collect(BACnetNetworks::new, BACnetNetworks::add);
    }

    @Override
    public DiscoveryLevel level() {
        return DiscoveryLevel.NETWORK;
    }

}
