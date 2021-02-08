package io.github.zero88.qwe.iot.data.enums;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;
import io.github.zero88.qwe.iot.data.IoTEnum;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class TransducerType extends AbstractEnumType implements IoTEnum {

    public static final TransducerType SENSOR = new TransducerType("SENSOR");
    public static final TransducerType ACTUATOR = new TransducerType("ACTUATOR");

    private TransducerType(String type) {
        super(type);
    }

    public static TransducerType def() { return SENSOR; }

    @JsonCreator
    public static TransducerType factory(String name) {
        return EnumType.factory(name, TransducerType.class, def());
    }

}
