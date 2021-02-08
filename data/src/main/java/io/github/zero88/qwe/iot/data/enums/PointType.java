package io.github.zero88.qwe.iot.data.enums;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;
import io.github.zero88.qwe.iot.data.IoTEnum;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class PointType extends AbstractEnumType implements IoTEnum {

    public static final PointType ANALOG_INPUT = new PointType("ANALOG_INPUT", "analog-input");
    public static final PointType ANALOG_OUTPUT = new PointType("ANALOG_OUTPUT", "analog-output");
    public static final PointType ANALOG_VALUE = new PointType("ANALOG_VALUE", "analog-value");

    public static final PointType BINARY_INPUT = new PointType("BINARY_INPUT", "binary-input");
    public static final PointType BINARY_OUTPUT = new PointType("BINARY_OUTPUT", "binary-output");
    public static final PointType BINARY_VALUE = new PointType("BINARY_VALUE", "binary-value");

    public static final PointType MULTI_STATE_INPUT = new PointType("MULTI_STATE_INPUT", "multi-state-input");
    public static final PointType MULTI_STATE_OUTPUT = new PointType("MULTI_STATE_OUTPUT", "multi-state-output");
    public static final PointType MULTI_STATE_VALUE = new PointType("MULTI_STATE_VALUE", "multi-state-value");

    public static final PointType COMMAND = new PointType("COMMAND", "command");
    public static final PointType CALENDAR = new PointType("CALENDAR", "calendar");
    public static final PointType SCHEDULE = new PointType("SCHEDULE", "schedule");

    public static final PointType UNKNOWN = new PointType("UNKNOWN");

    private PointType(String type)                    { super(type); }

    private PointType(String type, String... aliases) { super(type, aliases); }

    public static PointType def() {
        return UNKNOWN;
    }

    @JsonCreator
    public static PointType factory(String type) {
        return EnumType.factory(type, PointType.class, def());
    }

}
