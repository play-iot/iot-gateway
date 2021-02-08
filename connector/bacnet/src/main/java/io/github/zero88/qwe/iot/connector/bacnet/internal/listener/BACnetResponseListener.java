package io.github.zero88.qwe.iot.connector.bacnet.internal.listener;

import java.util.Optional;

import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.iot.connector.bacnet.internal.ack.AckServiceHandler;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetExceptionConverter;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.ResponseConsumer;
import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.Error;
import com.serotonin.bacnet4j.apdu.Reject;
import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.exception.RejectAPDUException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class BACnetResponseListener implements ResponseConsumer {

    @NonNull
    private final EventAction action;
    @NonNull
    private final Promise<EventMessage> msg;
    private final AckServiceHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public void success(AcknowledgementService ack) {
        msg.tryComplete(EventMessage.success(action, Optional.ofNullable(handler)
                                                             .map(h -> (JsonObject) h.apply(ack))
                                                             .orElseGet(JsonObject::new)));
    }

    @Override
    public void fail(AckAPDU ack) {
        msg.tryComplete(EventMessage.error(action, convert(convert(ack))));
    }

    @Override
    public void ex(BACnetException e) {
        msg.tryComplete(EventMessage.error(action, convert(e)));
    }

    private BACnetException convert(AckAPDU ack) {
        if (ack instanceof Error) {
            return new ErrorAPDUException((Error) ack);
        } else if (ack instanceof Reject) {
            return new RejectAPDUException((Reject) ack);
        } else if (ack instanceof Abort) {
            return new AbortAPDUException((Abort) ack);
        }
        return new BACnetException(Optional.ofNullable(ack).map(AckAPDU::toString).orElse("Unknown AckAPDU error"));
    }

    private ErrorMessage convert(BACnetException ex) {
        return ErrorMessage.parse(BACnetExceptionConverter.convert(ex));
    }

}
