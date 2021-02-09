package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import java.util.Optional;
import java.util.UUID;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.exceptions.AlreadyExistException;
import io.github.zero88.qwe.exceptions.NotFoundException;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.RemoteDeviceScanner;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetDeviceEntity;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetEntities.BACnetDevices;
import io.github.zero88.qwe.iot.data.entity.AbstractEntities;
import io.reactivex.Single;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.RemoteDeviceMixin;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class BACnetDeviceExplorer extends BACnetExplorer<ObjectIdentifier, BACnetDeviceEntity, BACnetDevices> {

    BACnetDeviceExplorer(@NonNull SharedDataLocalProxy sharedDataProxy) {
        super(sharedDataProxy);
    }

    @Override
    public Single<BACnetDeviceEntity> discover(RequestData reqData) {
        final DiscoveryArguments args = createDiscoveryArgs(reqData, level());
        final BACnetDevice device = getLocalDeviceFromCache(args);
        log.info("Discovering remote device {} in network {}...",
                 ObjectIdentifierMixin.serialize(args.params().remoteDeviceId()), device.protocol().identifier());
        return device.discoverRemoteDevice(args)
                     .flatMap(rd -> parseRemoteDevice(device, rd, true, args.options().isDetail()))
                     .doFinally(device::stop);
    }

    @Override
    public Single<BACnetDevices> discoverMany(RequestData reqData) {
        final DiscoveryArguments args = createDiscoveryArgs(reqData, DiscoveryLevel.NETWORK);
        final BACnetDevice device = getLocalDeviceFromCache(args);
        log.info("Discovering devices in network {}...", device.protocol().identifier());
        return device.scanRemoteDevices(args.options())
                     .map(RemoteDeviceScanner::getRemoteDevices)
                     .flattenAsObservable(r -> r)
                     .flatMapSingle(rd -> parseRemoteDevice(device, rd, args.options().isDetail(), false))
                     .collect(BACnetDevices::new, AbstractEntities::add)
                     .doFinally(device::stop);
    }

    @Override
    public DiscoveryLevel level() {
        return DiscoveryLevel.DEVICE;
    }

    private Single<BACnetDeviceEntity> parseRemoteDevice(@NonNull BACnetDevice device, @NonNull RemoteDevice rd,
                                                         boolean detail, boolean includeError) {
        final ObjectIdentifier objId = rd.getObjectIdentifier();
        final String networkCode = device.protocol().identifier();
        final String networkId = networkCache().getDataKey(networkCode).map(UUID::toString).orElse(networkCode);
        return device.parseRemoteObject(rd, objId, detail, includeError)
                     .map(pvm -> RemoteDeviceMixin.create(rd, pvm))
                     .map(rdm -> BACnetDeviceEntity.from(networkId, rdm));
    }

}
