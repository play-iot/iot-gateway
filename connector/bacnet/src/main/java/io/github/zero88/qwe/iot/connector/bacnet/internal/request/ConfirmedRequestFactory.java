package io.github.zero88.qwe.iot.connector.bacnet.internal.request;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.internal.ack.AckServiceHandler;
import io.reactivex.annotations.Nullable;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;

import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;

import lombok.NonNull;

/**
 * @param <S> Type of BACnet Confirmed Request
 * @param <A> Type of BACnet Acknowledgement
 * @param <D> Type of data to send BACnet remote device
 * @see ConfirmedRequestService
 */
public interface ConfirmedRequestFactory<S extends ConfirmedRequestService, A extends AcknowledgementService, D> {

    /**
     * Strict mode to determined raise exception if {@code AckConfirmedRequest} is error
     */
    String STRICT = "strict";

    @NonNull D convertData(@NonNull DiscoveryArguments args, @NonNull RequestData requestData);

    @NonNull S factory(@NonNull DiscoveryArguments args, @NonNull D data);

    default void then(@NonNull BACnetDevice device, @NonNull EventMessage result, @NonNull D data,
                      @NonNull DiscoveryArguments args, @NonNull RequestData requestData) {
        if (result.isError() && requestData.filter().getBoolean(STRICT, true)) {
            throw new CarlException(result.getError().getCode(), result.getError().getMessage());
        }
    }

    @Nullable
    default AckServiceHandler<A> handler() {
        return null;
    }

}
