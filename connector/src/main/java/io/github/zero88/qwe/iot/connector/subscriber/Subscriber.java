package io.github.zero88.qwe.iot.connector.subscriber;

import io.github.zero88.qwe.dto.JsonData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents for subscriber
 *
 * @see SubscriberType
 */
public interface Subscriber extends JsonData {

    @JsonUnwrapped
    SubscriberType getType();

    String getCode();

    @JsonProperty("key")
    default String key() {
        return getType().type() + "::" + getCode();
    }

}
