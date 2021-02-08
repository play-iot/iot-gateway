package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetEntity;
import io.github.zero88.qwe.iot.service.bacnet.service.AbstractBACnetService;
import io.github.zero88.qwe.iot.service.bacnet.service.BACnetApis;
import io.github.zero88.qwe.iot.connector.discovery.ExplorerServiceApis;
import io.github.zero88.qwe.iot.data.IoTEntities;
import io.reactivex.Single;

import lombok.NonNull;

/**
 * Defines BACnet explorer API service
 */
abstract class BACnetExplorer<K, P extends BACnetEntity<K>, X extends IoTEntities<K, P>> extends AbstractBACnetService
    implements BACnetApis, ExplorerServiceApis<K, P, X> {

    BACnetExplorer(@NonNull SharedDataLocalProxy sharedDataProxy) {
        super(sharedDataProxy);
    }

    @Override
    @EventContractor(action = "GET_ONE", returnType = Single.class)
    public abstract Single<P> discover(RequestData reqData);

    @Override
    @EventContractor(action = "GET_LIST", returnType = Single.class)
    public abstract Single<X> discoverMany(RequestData reqData);

}
