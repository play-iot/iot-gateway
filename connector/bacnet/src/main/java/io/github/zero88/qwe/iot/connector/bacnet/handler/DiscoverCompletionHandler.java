package io.github.zero88.qwe.iot.connector.bacnet.handler;

import io.github.zero88.qwe.component.ApplicationProbeHandler.ApplicationReadinessHandler;
import io.github.zero88.qwe.dto.ErrorData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventContractor;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents for {@code discover completion handler} that listens {@code success} or {@code error} event after scanning
 * network and initializing {@code local BACnet device}.
 *
 * @see BACnetDevice
 * @since 1.0.0
 */
@Slf4j
public class DiscoverCompletionHandler implements ApplicationReadinessHandler {

    @Override
    @EventContractor(action = "NOTIFY", returnType = boolean.class)
    public boolean success(@NonNull RequestData requestData) {
        log.info(requestData.toJson().encode());
        return true;
    }

    @Override
    @EventContractor(action = "NOTIFY_ERROR", returnType = boolean.class)
    public boolean error(@NonNull ErrorData error) {
        log.info(error.toJson().encode());
        return true;
    }

}
