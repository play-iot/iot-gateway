package io.github.zero88.qwe.iot.connector;

import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.protocol.HasProtocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class BaseProtocol implements HasSharedData, HasProtocol {

    @NonNull
    private final SharedDataLocalProxy sharedData;

}
