package io.github.zero88.qwe.iot.service.bacnet.service.discovery;

import java.io.IOException;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

import io.github.zero88.qwe.EventbusHelper;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.Status;
import io.github.zero88.qwe.exceptions.NetworkException;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetIP;
import io.github.zero88.qwe.protocol.network.IpNetwork;
import io.github.zero88.qwe.protocol.network.Ipv4Network;
import io.github.zero88.qwe.protocol.network.UdpProtocol;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import io.github.zero88.qwe.iot.service.bacnet.BACnetWithoutGatewayTest;

@Ignore
public class BACnetNetworkExplorerTest extends BACnetWithoutGatewayTest {

    @Test
    public void test_get_networks(TestContext context) {
        final Async async = context.async();
        Handler<JsonObject> handler = json -> {
            final EventMessage eventMessage = EventMessage.tryParse(json);
            try {
                context.assertEquals(Status.SUCCESS, eventMessage.getStatus());
                context.assertEquals(EventAction.GET_LIST, eventMessage.getAction());
                JsonObject ip = Optional.ofNullable(eventMessage.getData()).map(js -> {
                    System.out.println(js.encodePrettily());
                    return js.getJsonObject("ipv4", new JsonObject());
                }).orElseGet(JsonObject::new);
                context.assertNotEquals(0, ip.size());
            } finally {
                TestHelper.testComplete(async);
            }
        };
        eventbus.request(BACnetNetworkExplorer.class.getName(),
                         EventMessage.initial(EventAction.GET_LIST, new JsonObject()),
                         EventbusHelper.replyAsserter(context, handler));
    }

    @Test
    public void test_get_missing_network_code(TestContext context) {
        final Async async = context.async();
        final BACnetIP dockerIp = BACnetIP.builder().subnet("192.168.16.1/20").label("docker").build();
        final JsonObject body = new JsonObject().put("network", dockerIp.toJson());
        final JsonObject expected = new JsonObject(
            "{\"status\":\"FAILED\",\"action\":\"GET_ONE\",\"error\":{\"code\":\"INVALID_ARGUMENT\"," +
            "\"message\":\"Missing BACnet network code\"}}");
        eventbus.request(BACnetNetworkExplorer.class.getName(),
                         EventMessage.initial(EventAction.GET_ONE, RequestData.builder().body(body).build()),
                         EventbusHelper.replyAsserter(context, async, expected));
    }

    @Test
    public void test_get_available_network(TestContext context) throws IOException {
        final int randomPort = TestHelper.getRandomPort();
        final IpNetwork network = Ipv4Network.getActiveIps()
                                             .stream()
                                             .findFirst()
                                             .orElseThrow(() -> new NetworkException("Failed"));
        final Async async = context.async();
        final JsonObject request = new JsonObject().put("networkCode",
                                                        "udp4-" + network.getIfName() + "-" + randomPort);
        final JsonObject response = new JsonObject().put("network", UdpProtocol.builder()
                                                                               .ip(network)
                                                                               .port(randomPort)
                                                                               .canReusePort(false)
                                                                               .build()
                                                                               .toJson());
        final JsonObject expected = EventMessage.success(EventAction.GET_ONE, response).toJson();
        eventbus.request(BACnetNetworkExplorer.class.getName(),
                         EventMessage.initial(EventAction.GET_ONE, RequestData.builder().body(request).build()),
                         EventbusHelper.replyAsserter(context, async, expected));
    }

}
