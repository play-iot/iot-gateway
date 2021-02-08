package io.github.zero88.qwe.iot.connector.watcher;

import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.dto.EnumType.AbstractEnumType;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class WatcherType extends AbstractEnumType {

    public static final WatcherType POLLING = new WatcherType("POLLING");
    public static final WatcherType REALTIME = new WatcherType("REALTIME");

    private WatcherType(String type) {
        super(type);
    }

    @JsonCreator
    public static WatcherType factory(String type) {
        return EnumType.factory(type, WatcherType.class, true);
    }

}
