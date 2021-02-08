package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import io.github.zero88.qwe.EventbusHelper;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.iot.service.bacnet.BACnetWithoutGatewayTest;
import io.github.zero88.qwe.protocol.network.Ipv4Network;
import io.github.zero88.qwe.protocol.network.UdpProtocol;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

@Ignore
public class BACnetDeviceExplorerTest extends BACnetWithoutGatewayTest {

    @Test
    public void test_network_without_device(TestContext context) throws IOException {
        final Async async = context.async();
        final UdpProtocol protocol = UdpProtocol.builder()
                                                .ip(Ipv4Network.getFirstActiveIp())
                                                .port(TestHelper.getRandomPort())
                                                .build();
        final JsonObject body = new JsonObject().put("networkCode", protocol.identifier());
        final JsonObject expected = EventMessage.success(EventAction.GET_LIST,
                                                         new JsonObject().put("remoteDevices", new JsonArray()))
                                                .toJson();
        eventbus.request(BACnetDeviceExplorer.class.getName(),
                         EventMessage.initial(EventAction.GET_LIST, RequestData.builder().body(body).build()),
                         EventbusHelper.replyAsserter(context, async, expected));
    }

}
