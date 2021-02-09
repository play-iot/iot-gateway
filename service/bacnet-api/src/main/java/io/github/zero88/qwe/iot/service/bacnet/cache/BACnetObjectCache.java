package io.github.zero88.qwe.iot.service.bacnet.cache;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.github.zero88.qwe.cache.AbstractLocalCache;
import io.github.zero88.qwe.cache.LocalDataCache;
import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetObjectCache.BACnetObjectCacheKey;
import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetObjectCache.BACnetObjectCacheValue;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.github.zero88.utils.UUID64;

import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

//TODO more implementation
public final class BACnetObjectCache
    extends AbstractLocalCache<BACnetObjectCacheKey, BACnetObjectCacheValue, BACnetDeviceCache>
    implements LocalDataCache<BACnetObjectCacheKey, BACnetObjectCacheValue> {

    @Override
    protected String keyLabel() {
        return "BACnet device and protocol";
    }

    @Override
    protected String valueLabel() {
        return "BACnet objects";
    }

    @Override
    public BACnetObjectCache add(@NonNull BACnetObjectCacheKey key, @NonNull BACnetObjectCacheValue points) {
        cache().put(key, points);
        return this;
    }

    //    public BACnetObjectCache addDataKey(@NonNull CommunicationProtocol protocol, @NonNull ObjectIdentifier
    //    remoteDevice,
    //                                        @NonNull ObjectIdentifier objectCode, String dataPointId) {
    //        cache().computeIfPresent(new BACnetObjectCacheKey(protocol, remoteDevice), (key, value) -> {
    //            value.put(objectCode,
    //                      UUID64.uuidToBase64(Strings.requireNotBlank(dataPointId, "Missing data point point_id")));
    //            return value;
    //        });
    //        return this;
    //    }

    public Optional<UUID> getDataKey(@NonNull CommunicationProtocol protocol, @NonNull ObjectIdentifier remoteDevice,
                                     @NonNull ObjectIdentifier objectCode) {
        return Optional.ofNullable(cache().get(new BACnetObjectCacheKey(protocol, remoteDevice)))
                       .flatMap(cacheValue -> Optional.ofNullable(cacheValue.get(objectCode)))
                       .map(UUID64::uuid64ToUuid);
    }

    @Getter
    @RequiredArgsConstructor
    public static final class BACnetObjectCacheKey {

        @NonNull
        private final CommunicationProtocol protocol;
        @NonNull
        private final ObjectIdentifier deviceCode;

    }


    public static final class BACnetObjectCacheValue extends ConcurrentHashMap<ObjectIdentifier, String> {}

}
