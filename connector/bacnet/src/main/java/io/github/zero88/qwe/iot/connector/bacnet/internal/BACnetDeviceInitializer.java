package io.github.zero88.qwe.iot.connector.bacnet.internal;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.protocol.CommunicationProtocol;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import com.serotonin.bacnet4j.event.DeviceEventListener;

import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public final class BACnetDeviceInitializer {

    @NonNull
    private final SharedDataLocalProxy proxy;
    private final Consumer<BACnetDevice> preFunction;
    private final List<DeviceEventListener> listeners;

    public BACnetDevice asyncStart(@NonNull CommunicationProtocol protocol) {
        final BACnetDevice device = new DefaultBACnetDevice(proxy, protocol);
        Optional.ofNullable(preFunction).ifPresent(f -> f.accept(device));
        Optional.ofNullable(listeners).ifPresent(device::addListeners);
        return device.asyncStart();
    }

}
