package io.github.zero88.qwe.iot.data.enums;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;
import io.github.zero88.qwe.iot.data.IoTEnum;
import io.github.zero88.utils.Strings;

import com.fasterxml.jackson.annotation.JsonValue;

public final class DeviceStatus extends AbstractEnumType implements IoTEnum {

    public static final DeviceStatus UP = new DeviceStatus("UP");
    public static final DeviceStatus DOWN = new DeviceStatus("DOWN");
    public static final DeviceStatus BUSY = new DeviceStatus("BUSY");
    public static final DeviceStatus UNREACHABLE = new DeviceStatus("UNREACHABLE");
    public static final DeviceStatus UNKNOWN = new DeviceStatus("UNKNOWN");

    private DeviceStatus(String type) {
        super(type);
    }

    public static DeviceStatus parse(String action) {
        return Strings.isBlank(action) ? UNKNOWN : EnumType.factory(action, DeviceStatus.class);
    }

    @JsonValue
    public String status() {
        return this.type();
    }

}
