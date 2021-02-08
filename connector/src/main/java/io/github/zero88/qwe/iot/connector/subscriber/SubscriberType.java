package io.github.zero88.qwe.iot.connector.subscriber;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class SubscriberType extends AbstractEnumType {

    private SubscriberType(String type) {
        super(type);
    }

    @JsonCreator
    public static SubscriberType factory(String name) {
        return EnumType.factory(name, SubscriberType.class, true);
    }

}
