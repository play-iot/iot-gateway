package io.github.zero88.qwe.iot.connector.bacnet.internal;

import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetMSTP;
import io.github.zero88.qwe.protocol.serial.SerialPortProtocol;

import com.serotonin.bacnet4j.transport.DefaultTransport;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class TransportMstp implements TransportProvider {

    @NonNull
    private final SerialPortProtocol protocol;

    static TransportIP byConfig(BACnetMSTP config) {
        //    private Transport buildTransportMSTP(String port, int macNum, int bufferSize, int retryCount) throws
        //    Exception {
        //        byte[] bytes = new byte[bufferSize];
        //        MstpNode node = new MasterNode(port, new ByteArrayInputStream(bytes), new ByteArrayOutputStream(),
        //                                       (byte) macNum, retryCount);
        //        MstpNetwork network = new MstpNetwork(node, );
        //        return new DefaultTransport(network);
        //    }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public SerialPortProtocol protocol() {
        return protocol;
    }

    @Override
    public DefaultTransport get() {
        protocol.isReachable();
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
