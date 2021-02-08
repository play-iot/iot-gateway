package io.github.zero88.qwe.iot.connector.bacnet.entity;

import io.github.zero88.qwe.protocol.network.Ethernet;
import io.github.zero88.qwe.protocol.network.TcpProtocol;
import io.github.zero88.qwe.protocol.network.UdpProtocol;
import io.github.zero88.qwe.utils.Networks;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = BACnetIP.Builder.class)
public final class BACnetIP extends BACnetNetwork {

    public static final String TYPE = "BACnetIP";
    private final String subnet;
    private final String networkInterface;
    private final String hostAddress;
    private final String macAddress;
    private final int port;

    private BACnetIP(String label, String subnet, String networkInterface, String hostAddress, String macAddress,
                     int port) {
        super(TYPE, label);
        this.subnet = subnet;
        this.networkInterface = networkInterface;
        this.hostAddress = hostAddress;
        this.macAddress = macAddress;
        this.port = Networks.validPort(port, IpNetwork.DEFAULT_PORT);
    }

    @Override
    public @NonNull UdpProtocol toProtocol() {
        return UdpProtocol.builder()
                          .port(this.port)
                          .ifName(this.networkInterface)
                          .cidrAddress(this.subnet)
                          .displayName(label())
                          .canReusePort(true)
                          .build();
    }

    static BACnetIP from(@NonNull Ethernet ethernet) {
        if (ethernet instanceof TcpProtocol) {
            throw new IllegalArgumentException("BACnet protocol is not working on TCP");
        }
        final int port = ethernet instanceof UdpProtocol ? ((UdpProtocol) ethernet).getPort() : IpNetwork.DEFAULT_PORT;
        return BACnetIP.builder()
                       .label(ethernet.getDisplayName())
                       .networkInterface(ethernet.getIfName())
                       .subnet(ethernet.getCidrAddress())
                       .hostAddress(ethernet.getHostAddress())
                       .macAddress(ethernet.getMacAddress())
                       .port(port)
                       .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends BACnetNetworkBuilder<BACnetIP, BACnetIP.Builder> {

        public BACnetIP build() {
            return new BACnetIP(label, subnet, networkInterface, hostAddress, macAddress, port);
        }

    }

}
