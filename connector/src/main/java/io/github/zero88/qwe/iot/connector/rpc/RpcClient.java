package io.github.zero88.qwe.iot.connector.rpc;

import io.github.zero88.qwe.protocol.HasProtocol;
import io.github.zero88.qwe.rpc.GatewayServiceInvoker;

import lombok.NonNull;

public interface RpcClient extends HasProtocol, GatewayServiceInvoker {

    @NonNull String function();

    @Override
    @NonNull
    default String requester() {
        return protocol().type() + "-" + function();
    }

    @Override
    default String serviceLabel() {
        return protocol().type() + function() + " RPC client";
    }

}
