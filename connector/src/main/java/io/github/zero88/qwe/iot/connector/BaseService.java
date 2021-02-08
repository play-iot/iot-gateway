package io.github.zero88.qwe.iot.connector;

import io.github.zero88.qwe.component.SharedDataLocalProxy;

import lombok.NonNull;

public abstract class BaseService extends BaseProtocol implements ConnectorService {

    protected BaseService(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

}
