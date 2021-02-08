package io.github.zero88.qwe.iot.connector.bacnet.internal.listener;

import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.service.Service;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.constructed.Address;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WhoIsListener extends DeviceEventAdapter implements DeviceEventListener {

    @Override
    public void requestReceived(Address from, Service service) {
        if (log.isDebugEnabled()) {
            log.debug("Address: {} - Global {} | Service: {} - {} - {}", from.getDescription(), from.isGlobal(),
                      service.getChoiceId(), service.getNetworkPriority(), service.getClass());
        }
        if (service instanceof WhoIsRequest) {
            log.info("Received WhoIs from {}", from.toString());
        }
    }

}
