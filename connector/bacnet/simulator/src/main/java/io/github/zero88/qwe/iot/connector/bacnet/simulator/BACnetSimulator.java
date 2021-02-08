package io.github.zero88.qwe.iot.connector.bacnet.simulator;

import java.util.Optional;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetApplication;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.connector.bacnet.handler.DiscoverCompletionHandler;
import io.github.zero88.qwe.iot.connector.bacnet.internal.listener.WhoIsListener;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BACnetSimulator extends BACnetApplication<SimulatorConfig> {

    private final DiscoverCompletionHandler handler;

    @Override
    @NonNull
    protected Class<SimulatorConfig> bacnetConfigClass() {
        return SimulatorConfig.class;
    }

    @Override
    protected void addListenerOnEachDevice(@NonNull BACnetDevice device) {
        device.addListeners(new WhoIsListener());
    }

    @Override
    protected DiscoverCompletionHandler createDiscoverCompletionHandler() {
        return Optional.ofNullable(handler).orElse(super.createDiscoverCompletionHandler());
    }

}
