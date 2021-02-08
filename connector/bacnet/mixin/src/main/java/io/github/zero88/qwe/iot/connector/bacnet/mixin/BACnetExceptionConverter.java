package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import java.util.Objects;

import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.ErrorCode;
import io.github.zero88.qwe.exceptions.TimeoutException;
import io.github.zero88.qwe.exceptions.converter.CarlExceptionConverter;

import com.serotonin.bacnet4j.exception.AbortAPDUException;
import com.serotonin.bacnet4j.exception.BACnetAbortException;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRejectException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.exception.ErrorAPDUException;
import com.serotonin.bacnet4j.exception.RejectAPDUException;
import com.serotonin.bacnet4j.exception.SegmentedMessageAbortedException;
import com.serotonin.bacnet4j.type.Encodable;

import lombok.NonNull;

public final class BACnetExceptionConverter {

    public static final ErrorCode BACNET_ERROR = ErrorCode.parse("BACNET_ERROR");

    public static CarlException convert(@NonNull BACnetException throwable) {
        if (throwable instanceof BACnetTimeoutException) {
            return new TimeoutException(throwable.getMessage(), throwable);
        }
        //        if (throwable instanceof ServiceTooBigException) {
        //            return new ServiceTooBigException(throwable.getMessage());
        //        }
        Encodable reason = null;
        if (throwable instanceof AbortAPDUException) {
            reason = ((AbortAPDUException) throwable).getApdu().getAbortReason();
        }
        if (throwable instanceof BACnetAbortException) {
            reason = ((BACnetAbortException) throwable).getAbortReason();
        }
        if (throwable instanceof BACnetErrorException) {
            reason = ((BACnetErrorException) throwable).getBacnetError();
        }
        if (throwable instanceof BACnetRejectException) {
            reason = ((BACnetRejectException) throwable).getRejectReason();
        }
        if (throwable instanceof RejectAPDUException) {
            reason = ((RejectAPDUException) throwable).getApdu().getRejectReason();
        }
        if (throwable instanceof BACnetRejectException) {
            reason = ((BACnetRejectException) throwable).getRejectReason();
        }
        if (throwable instanceof ErrorAPDUException) {
            reason = ((ErrorAPDUException) throwable).getError();
        }
        if (throwable instanceof SegmentedMessageAbortedException) {
            reason = ((SegmentedMessageAbortedException) throwable).getAbort().getAbortReason();
        }
        if (Objects.isNull(reason)) {
            return CarlExceptionConverter.friendly(throwable);
        }
        return new CarlException(BACNET_ERROR, reason.toString(), throwable);
    }

}
