package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import java.util.Optional;
import java.util.UUID;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.exceptions.AlreadyExistException;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.ErrorCode;
import io.github.zero88.qwe.exceptions.NotFoundException;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetEntities.BACnetPoints;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetPointEntity;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import io.github.zero88.qwe.iot.data.entity.AbstractEntities;
import io.github.zero88.utils.Functions;
import io.reactivex.Observable;
import io.reactivex.Single;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BACnetObjectExplorer extends BACnetExplorer<ObjectIdentifier, BACnetPointEntity, BACnetPoints> {

    BACnetObjectExplorer(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    @Override
    public Single<BACnetPointEntity> discover(RequestData requestData) {
        final DiscoveryArguments args = createDiscoveryArgs(requestData, level());
        final BACnetDevice device = getLocalDeviceFromCache(args);
        return device.discoverRemoteObject(args)
                     .map(pvm -> BACnetPointEntity.from(args.params().getNetworkId(), args.params().remoteDeviceId(),
                                                        pvm));
    }

    @Override
    public Single<BACnetPoints> discoverMany(RequestData requestData) {
        final DiscoveryArguments args = createDiscoveryArgs(requestData, DiscoveryLevel.DEVICE);
        final BACnetDevice device = getLocalDeviceFromCache(args);
        log.info("Discovering objects in device '{}' in network {}...",
                 ObjectIdentifierMixin.serialize(args.params().remoteDeviceId()), device.protocol().identifier());
        return device.discoverRemoteDevice(args)
                     .flatMap(remote -> getRemoteObjects(device, remote, args.options().isDetail()))
                     .doFinally(device::stop);
    }

    @Override
    public DiscoveryLevel level() {
        return DiscoveryLevel.OBJECT;
    }

    private Single<BACnetPoints> getRemoteObjects(@NonNull BACnetDevice device, @NonNull RemoteDevice rd,
                                                  boolean detail) {
        return Observable.fromIterable(Functions.getOrThrow(t -> new CarlException(ErrorCode.SERVICE_ERROR, t),
                                                            () -> RequestUtils.getObjectList(device.localDevice(), rd)))
                         .filter(oid -> oid.getObjectType() != ObjectType.device)
                         .flatMapSingle(oid -> device.parseRemoteObject(rd, oid, detail, false))
                         .map(pvm -> BACnetPointEntity.from(device.protocol().identifier(), rd.getObjectIdentifier(),
                                                            pvm))
                         .collect(BACnetPoints::new, AbstractEntities::add)
                         .doFinally(device::stop);
    }

}
