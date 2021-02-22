package io.github.zero88.iot.connector.kafka.handler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.Status;
import io.github.zero88.qwe.exceptions.ErrorCode;
import io.github.zero88.utils.Strings;
import io.vertx.core.buffer.Buffer;
import io.vertx.kafka.client.producer.KafkaHeader;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @see Headers
 * @see KafkaHeader
 */
@Slf4j
public final class KafkaHeaderConverter {

    private static final String ERROR_CODE = "qwe.error.code";
    private static final String ERROR_MESSAGE = "qwe.error.message";
    private static final String STATUS = "qwe.status";
    private static final String PREV_ACTION = "qwe.prevAction";
    private static final String ACTION = "qwe.action";

    public static List<KafkaHeader> convert(@NonNull EventMessage message) {
        List<KafkaHeader> headers = new ArrayList<>();
        headers.add(KafkaHeader.header(ACTION, message.getAction().type()));
        headers.add(KafkaHeader.header(STATUS, message.getStatus().name()));
        ErrorMessage error = message.getError();
        if (message.isError()) {
            headers.add(KafkaHeader.header(ERROR_CODE, error.getCode().code()));
            headers.add(KafkaHeader.header(ERROR_MESSAGE, error.getMessage()));
        }
        return headers;
    }

    public static List<KafkaHeader> convert(Map<String, Object> headerMaps) {
        List<KafkaHeader> headers = new ArrayList<>();
        if (Objects.nonNull(headerMaps)) {
            headerMaps.forEach((key, value) -> headers.add(
                KafkaHeader.header(key, Buffer.buffer(Strings.toString(value), StandardCharsets.UTF_8.name()))));
        }
        return headers;
    }

    public static EventMessage convert(@NonNull Headers headers) {
        EventAction action = getHeader(headers, ACTION, EventAction.UNKNOWN);
        EventAction prevAction = getHeader(headers, PREV_ACTION, EventAction.UNKNOWN);
        Status status = Enum.valueOf(Status.class, getHeader(headers, STATUS));
        ErrorMessage error = status == Status.FAILED ? getHeader(headers) : null;
        return Objects.isNull(error)
               ? EventMessage.from(status, action, prevAction)
               : EventMessage.error(action, prevAction, error);
    }

    @SuppressWarnings("unchecked")
    private static <T extends EnumType> T getHeader(@NonNull Headers headers, @NonNull String key,
                                                    @NonNull T fallback) {
        try {
            return EnumType.factory(getHeader(headers, key), (Class<T>) fallback.getClass(), fallback);
        } catch (IllegalArgumentException e) {
            log.trace("Return fallback value after failed in converting enum class" + fallback.getClass(), e);
            return fallback;
        }
    }

    private static ErrorMessage getHeader(@NonNull Headers headers) {
        ErrorCode code = getHeader(headers, ERROR_CODE, (ErrorCode) ErrorCode.UNKNOWN_ERROR);
        String message = getHeader(headers, ERROR_MESSAGE);
        return ErrorMessage.parse(code, message);
    }

    private static String getHeader(@NonNull Headers headers, String key) {
        Header header = headers.lastHeader(key);
        if (Objects.isNull(header) || Objects.isNull(header.value())) {
            return "";
        }
        return new String(header.value(), StandardCharsets.UTF_8);
    }

}
