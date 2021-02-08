package io.github.zero88.qwe.iot.connector.bacnet.discovery;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DiscoveryLevel {

    NETWORK(10), DEVICE(20), OBJECT(30);

    private final int level;

    boolean mustValidate(@NonNull DiscoveryLevel given) {
        return this.level <= given.level;
    }
}
