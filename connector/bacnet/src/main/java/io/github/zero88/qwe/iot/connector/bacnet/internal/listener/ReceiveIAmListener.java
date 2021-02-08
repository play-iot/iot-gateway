package io.github.zero88.qwe.iot.connector.bacnet.internal.listener;

import java.util.function.Consumer;

import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final class ReceiveIAmListener extends DeviceEventAdapter {

    @NonNull
    private final Consumer<RemoteDevice> handler;

    @Override
    public void iAmReceived(RemoteDevice d) {
        handler.andThen(this::log).accept(d);
    }

    private void log(RemoteDevice d) {
        log.info("Receive IAm from Instance: {} - Address: {}", d.getInstanceNumber(), d.getAddress().getDescription());
    }

}
