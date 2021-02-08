package io.github.zero88.qwe.iot.connector.bacnet.internal;

import java.util.function.Supplier;

import io.github.zero88.qwe.exceptions.CommunicationProtocolException;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetIP;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetNetwork;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.github.zero88.qwe.protocol.network.UdpProtocol;
import io.github.zero88.qwe.protocol.serial.SerialPortProtocol;

import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetMSTP;

import com.serotonin.bacnet4j.transport.Transport;

import lombok.NonNull;

public interface TransportProvider extends Supplier<Transport> {

    static TransportProvider byConfig(@NonNull BACnetNetwork config) {
        if (config instanceof BACnetIP) {
            return TransportIP.byConfig((BACnetIP) config);
        }
        if (config instanceof BACnetMSTP) {
            return TransportMstp.byConfig((BACnetMSTP) config);
        }
        throw new IllegalArgumentException(
            "Does not support BACNet network config type " + config.getClass().getName());
    }

    static TransportProvider byProtocol(@NonNull CommunicationProtocol protocol) {
        if (protocol instanceof UdpProtocol) {
            return new TransportIP((UdpProtocol) protocol);
        }
        if (protocol instanceof SerialPortProtocol) {
            return new TransportMstp((SerialPortProtocol) protocol);
        }
        throw new IllegalArgumentException("Does not support BACNet protocol type " + protocol.type());
    }

    /**
     * Get {@code protocol} is holden by transport provider
     *
     * @return current {@code protocol}
     */
    CommunicationProtocol protocol();

    /**
     * Get {@code transport} by {@link #protocol()} computation
     *
     * @return BACnet transport
     * @throws CommunicationProtocolException if {@code protocol} is unreachable
     */
    @Override
    Transport get();

}
