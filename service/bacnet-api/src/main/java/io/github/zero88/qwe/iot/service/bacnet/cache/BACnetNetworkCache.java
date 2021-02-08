package io.github.zero88.qwe.iot.service.bacnet.cache;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.github.zero88.qwe.cache.AbstractLocalCache;
import io.github.zero88.qwe.cache.LocalDataCache;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.github.zero88.qwe.protocol.network.Ipv4Network;
import io.github.zero88.qwe.protocol.network.Ipv6Network;
import io.github.zero88.qwe.protocol.serial.SerialPortProtocol;
import io.github.zero88.utils.Strings;
import io.github.zero88.utils.UUID64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BACnetNetworkCache extends AbstractLocalCache<String, CommunicationProtocol, BACnetNetworkCache>
    implements LocalDataCache<String, CommunicationProtocol> {

    private final ConcurrentMap<String, String> dataPointCache = new ConcurrentHashMap<>();

    static BACnetNetworkCache init() {
        return rescan(new BACnetNetworkCache().register(CommunicationProtocol::parse));
    }

    public static BACnetNetworkCache rescan(@NonNull BACnetNetworkCache cache) {
        Ipv4Network.getActiveIps().forEach(ipv4 -> cache.add(ipv4.identifier(), ipv4));
        Ipv6Network.getActiveIps().forEach(ipv6 -> cache.add(ipv6.identifier(), ipv6));
        SerialPortProtocol.getActivePorts().forEach(serialPort -> cache.add(serialPort.identifier(), serialPort));
        return cache;
    }

    @Override
    protected String keyLabel() {
        return "Protocol identifier";
    }

    @Override
    protected String valueLabel() {
        return CommunicationProtocol.class.getSimpleName();
    }

    @Override
    public BACnetNetworkCache add(@NonNull String key, @NonNull CommunicationProtocol protocol) {
        cache().put(key, protocol);
        return this;
    }

    public BACnetNetworkCache addDataKey(@NonNull CommunicationProtocol protocol, String dataPointKey) {
        if (Objects.isNull(get(protocol.identifier()))) {
            throw new IllegalArgumentException("Invalid or unreachable network " + protocol.identifier());
        }
        dataPointCache.put(protocol.identifier(),
                           UUID64.uuidToBase64(Strings.requireNotBlank(dataPointKey, "Missing data point network_id")));
        return this;
    }

    public Optional<UUID> getDataKey(@NonNull String key) {
        return Optional.ofNullable(dataPointCache.get(key)).map(UUID64::uuid64ToUuid);
    }

}
