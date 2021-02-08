package io.github.zero88.qwe.iot.connector.rpc.persistence;

import io.github.zero88.qwe.iot.connector.rpc.RpcClient;

import lombok.NonNull;

public interface PersistenceClient extends RpcClient {

    @Override
    default @NonNull String function() {
        return "persistence";
    }

    /**
     * A persistence service name
     *
     * @return persistence service name
     */
    @Override
    @NonNull String destination();

}
