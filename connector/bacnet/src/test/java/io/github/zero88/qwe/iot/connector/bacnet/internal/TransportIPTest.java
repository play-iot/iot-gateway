package io.github.zero88.qwe.iot.connector.bacnet.internal;

import org.junit.Assert;
import org.junit.Test;

import io.github.zero88.qwe.exceptions.CommunicationProtocolException;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetIP;
import io.github.zero88.qwe.protocol.network.Ipv4Network;

public class TransportIPTest {

    @Test
    public void test_by_subnet() {
        final Ipv4Network ip = Ipv4Network.getFirstActiveIp();
        final TransportIP transportIP = TransportIP.byConfig(BACnetIP.builder().subnet(ip.getCidrAddress()).build());
        Assert.assertEquals(47808, transportIP.protocol().getPort());
        Assert.assertEquals(ip.getCidrAddress(), transportIP.protocol().getCidrAddress());
        Assert.assertNull(transportIP.protocol().getIfName());
        transportIP.get();
        Assert.assertNotNull(transportIP.protocol().getIfName());
        Assert.assertEquals(ip, transportIP.protocol().getIp());
        Assert.assertTrue(transportIP.protocol().isCanReusePort());
    }

    @Test
    public void test_by_network() {
        final Ipv4Network ip = Ipv4Network.getFirstActiveIp();
        final TransportIP transportIP = TransportIP.byConfig(
            BACnetIP.builder().networkInterface(ip.getIfName()).port(9999).build());
        Assert.assertEquals(9999, transportIP.protocol().getPort());
        Assert.assertEquals(ip.getIfName(), transportIP.protocol().getIfName());
        Assert.assertNull(transportIP.protocol().getCidrAddress());
        transportIP.get();
        Assert.assertEquals(ip, transportIP.protocol().getIp());
        Assert.assertTrue(transportIP.protocol().isCanReusePort());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_by_subnet_invalid() {
        TransportIP.byConfig(BACnetIP.builder().subnet("456.168.6.1/24").build()).get();
    }

    @Test(expected = CommunicationProtocolException.class)
    public void test_by_network_invalid() {
        TransportIP.byConfig(BACnetIP.builder().networkInterface("not_found_xxx").build()).get();
    }

    @Test
    public void test_by_network_null() {
        final Ipv4Network ip = Ipv4Network.getFirstActiveIp();
        final TransportIP transportIP = TransportIP.byConfig(BACnetIP.builder().build());
        Assert.assertEquals("udp4", transportIP.protocol().type());
        Assert.assertNull(transportIP.protocol().getIfName());
        Assert.assertNull(transportIP.protocol().getCidrAddress());
        Assert.assertNotEquals(ip, transportIP.protocol().getIp());
        transportIP.get();
        Assert.assertEquals(ip, transportIP.protocol().getIp());
        Assert.assertTrue(transportIP.protocol().isCanReusePort());
    }

}
