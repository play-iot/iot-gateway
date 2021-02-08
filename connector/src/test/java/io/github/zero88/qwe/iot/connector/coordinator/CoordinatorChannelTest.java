package io.github.zero88.qwe.iot.connector.coordinator;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.mock.MockSubject;
import io.github.zero88.qwe.iot.connector.mock.MockSubscriber;
import io.github.zero88.qwe.iot.connector.watcher.WatcherType;
import io.vertx.core.json.JsonObject;

public class CoordinatorChannelTest {

    @Test
    void test_serialize() throws JSONException {
        final CoordinatorInput<MockSubject> input = CoordinatorInput.<MockSubject>builder().subject(
            new MockSubject("m1")).subscriber(new MockSubscriber("s1")).build();
        final CoordinatorChannel channel = CoordinatorChannel.from(input, WatcherType.REALTIME, "watcherKey",
                                                                   new JsonObject().put("a", "b"));
        final JsonObject expected = new JsonObject(
            "{\"subject\":{\"key\":\"m1\"},\"watcherKey\":\"watcherKey\",\"watcherType\":\"REALTIME\"," +
            "\"watcherOption\":{\"realtime\":true,\"lifetimeInSeconds\":-1,\"fallbackPolling\":true," +
            "\"polling\":false,\"triggerOption\":{\"type\":\"PERIODIC\",\"intervalInSeconds\":5,\"repeat\":-1}}," +
            "\"subscribers\":[{\"code\":\"s1\",\"type\":\"MOCK\",\"key\":\"MOCK::s1\"}],\"key\":\"m1\", " +
            "\"watcherOutput\":{\"a\":\"b\"}}");
        JsonHelper.assertJson(expected, channel.toJson());
    }

    @Test
    void test_deserialize() {
        JsonObject input = new JsonObject(
            "{\"watcherOutput\":{\"jobKey\":\"BACnet.udp4-wlp4s0-47808_1110_device:1110\"," +
            "\"triggerKey\":\"BACnet.udp4-wlp4s0-47808_1110_device:1110\"," +
            "\"firstFireTime\":{\"local\":\"2021-02-04T17:25:26.072+07:00[Asia/Ho_Chi_Minh]\"," +
            "\"utc\":\"2021-02-04T10:25:26.072Z\"}},\"subject\":{\"networkId\":\"udp4-wlp4s0-47808\"," +
            "\"deviceInstance\":1110,\"objectCode\":\"device:1110\"," +
            "\"key\":\"udp4-wlp4s0-47808_1110_device:1110\"},\"subscribers\":[{\"wsPath\":\"/cov\"," +
            "\"publishAddress\":\"bacnet.websocket.cov\",\"action\":\"MONITOR\",\"code\":\"bacnet_cov\"," +
            "\"type\":\"WEBSOCKET_SERVER\",\"key\":\"WEBSOCKET_SERVER::bacnet_cov\"}]," +
            "\"watcherOption\":{\"realtime\":true,\"lifetimeInSeconds\":-1,\"fallbackPolling\":true," +
            "\"polling\":false,\"triggerOption\":{\"type\":\"PERIODIC\",\"intervalInSeconds\":5," +
            "\"repeat\":-1}},\"watcherType\":\"POLLING\"}");
        final CoordinatorChannel from = JsonData.from(input, CoordinatorChannel.class);
        Assertions.assertEquals("udp4-wlp4s0-47808_1110_device:1110", from.key());
        Assertions.assertEquals(WatcherType.POLLING, from.getWatcherType());
        Assertions.assertEquals(1, from.getSubscribers().size());
        System.out.println(from.toJson());
    }

}
