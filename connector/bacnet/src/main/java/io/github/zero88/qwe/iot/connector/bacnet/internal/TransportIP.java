package io.github.zero88.qwe.iot.connector.bacnet.internal;

import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetIP;
import io.github.zero88.qwe.protocol.network.UdpProtocol;

import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.npdu.ipv6.Ipv6NetworkBuilder;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class TransportIP implements TransportProvider {

    @NonNull
    private final UdpProtocol protocol;

    static TransportIP byConfig(@NonNull BACnetIP config) {
        return new TransportIP(config.toProtocol());
    }

    @Override
    public UdpProtocol protocol() {
        return protocol;
    }

    @Override
    public Transport get() {
        protocol.isReachable();
        if (protocol.getIp().version() == 4) {
            final IpNetwork network = new IpNetworkBuilder().withPort(protocol.getPort())
                                                            .withReuseAddress(true)
                                                            .withSubnet(protocol.getIp().getSubnetAddress(),
                                                                        protocol.getIp().getSubnetPrefixLength())
                                                            .build();
            return new DefaultTransport(network);
        }
        return new DefaultTransport(new Ipv6NetworkBuilder(protocol.getCidrAddress()).port(protocol.getPort()).build());
    }

}
