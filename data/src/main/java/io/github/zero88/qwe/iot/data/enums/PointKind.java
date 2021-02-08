package io.github.zero88.qwe.iot.data.enums;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;
import io.github.zero88.qwe.iot.data.IoTEnum;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class PointKind extends AbstractEnumType implements IoTEnum {

    public static final PointKind UNKNOWN = new PointKind("UNKNOWN");

    private PointKind(String type) {
        super(type);
    }

    public static PointKind def() { return UNKNOWN; }

    @JsonCreator
    public static PointKind factory(String name) {
        return EnumType.factory(name, PointKind.class, def());
    }

}
