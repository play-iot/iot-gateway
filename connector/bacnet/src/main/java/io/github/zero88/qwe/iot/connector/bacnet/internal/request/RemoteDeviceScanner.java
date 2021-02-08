package io.github.zero88.qwe.iot.connector.bacnet.internal.request;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryOptions;
import io.github.zero88.qwe.iot.connector.bacnet.internal.listener.ReceiveIAmListener;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.cache.RemoteEntityCachePolicy;
import com.serotonin.bacnet4j.cache.RemoteEntityCachePolicy.TimedExpiry;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder(builderClassName = "Builder")
public final class RemoteDeviceScanner {

    @NonNull
    private final LocalDevice localDevice;
    private final int minDevice;
    private final int maxDevice;
    private final ReceiveIAmListener listener;

    public static RemoteDeviceScanner create(@NonNull LocalDevice localDevice, @NonNull DiscoveryOptions options) {
        final RemoteEntityCachePolicy policy = Optional.ofNullable(options.getDuration())
                                                       .map(TimedExpiry::new)
                                                       .map(RemoteEntityCachePolicy.class::cast)
                                                       .orElse(RemoteEntityCachePolicy.NEVER_EXPIRE);
        Consumer<RemoteDevice> consumer = remoteDevice -> localDevice.getRemoteDeviceCache()
                                                                     .putEntity(remoteDevice.getInstanceNumber(),
                                                                                remoteDevice, policy);
        int maxDevice = options.getMaxItem() == -1 ? ObjectIdentifier.UNINITIALIZED : options.getMaxItem();
        return RemoteDeviceScanner.builder().localDevice(localDevice)
                                  .listener(new ReceiveIAmListener(consumer))
                                  .minDevice(options.getMinItem())
                                  .maxDevice(maxDevice)
                                  .build();
    }

    public RemoteDeviceScanner start() {
        log.info("Send Global broadcast then capture remote device {}-{}", minDevice, maxDevice);
        localDevice.getEventHandler().addListener(listener);
        localDevice.sendGlobalBroadcast(new WhoIsRequest(minDevice, maxDevice));
        return this;
    }

    public void stop() {
        localDevice.getEventHandler().removeListener(listener);
    }

    public List<RemoteDevice> getRemoteDevices() {
        return localDevice.getRemoteDevices()
                          .stream()
                          .filter(remoteDevice -> remoteDevice.getInstanceNumber() >= minDevice &&
                                                  remoteDevice.getInstanceNumber() <= maxDevice)
                          .collect(Collectors.toList());
    }

}
