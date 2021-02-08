package io.github.zero88.qwe.iot.connector.bacnet;

import io.github.zero88.qwe.protocol.HasProtocol;
import io.github.zero88.qwe.protocol.Protocol;

import lombok.NonNull;

public interface BACnetProtocol extends HasProtocol {

    @Override
    default @NonNull Protocol protocol() {
        return Protocol.BACnet;
    }

}
