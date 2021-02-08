package io.github.zero88.qwe.iot.connector.watcher;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.scheduler.model.trigger.TriggerOption;
import io.github.zero88.qwe.scheduler.model.trigger.TriggerType;
import io.vertx.core.json.JsonObject;

public class WatcherOptionTest {

    @Test
    public void test_default() throws JSONException {
        final WatcherOption option = WatcherOption.builder().build();
        final JsonObject expected = new JsonObject(
            "{\"realtime\":true,\"lifetimeInSeconds\":-1,\"fallbackPolling\":true,\"polling\":false," +
            "\"triggerOption\":{\"type\":\"PERIODIC\",\"intervalInSeconds\":5,\"repeat\":-1}}");
        JsonHelper.assertJson(expected, option.toJson());
        final WatcherOption from = JsonData.from(expected, WatcherOption.class);
        Assertions.assertTrue(from.isRealtime());
        Assertions.assertFalse(from.isPolling());
        Assertions.assertTrue(from.isFallbackPolling());
        Assertions.assertEquals(TriggerType.PERIODIC, from.getTriggerOption().getType());
        Assertions.assertEquals(5, from.getTriggerOption().getIntervalInSeconds());
        Assertions.assertEquals(-1, from.getTriggerOption().getRepeat());
        Assertions.assertNull(from.getTriggerOption().getExpression());
        Assertions.assertNull(from.getTriggerOption().getTimezone());
    }

    @Test
    public void test_serialize() throws JSONException {
        final TriggerOption trigger = TriggerOption.builder()
                                                   .type(TriggerType.CRON)
                                                   .timezone("UTC+7")
                                                   .expression("* * * * * ? *")
                                                   .build();
        final WatcherOption option = WatcherOption.builder()
                                                  .realtime(false)
                                                  .lifetimeInSeconds(10)
                                                  .fallbackPolling(false)
                                                  .polling(true)
                                                  .triggerOption(trigger)
                                                  .build();
        final JsonObject expected = new JsonObject(
            "{\"realtime\":false,\"lifetimeInSeconds\":10,\"fallbackPolling\":false,\"polling\":true," +
            "\"triggerOption\":{\"type\":\"CRON\",\"expression\":\"* * * * * ? *\",\"timezone\":\"UTC+7\"," +
            "\"repeat\":-1}}");
        JsonHelper.assertJson(expected, option.toJson());
    }

    @Test
    public void test_deserialize() {
        final JsonObject json = new JsonObject(
            "{\"realtime\":true,\"lifetimeInSeconds\":10,\"fallbackPolling\":true,\"polling\":true," +
            "\"triggerOption\":{\"type\":\"CRON\",\"expression\":\"* 0 * ? * * *\",\"timezone\":\"UTC+10\"}}");
        final WatcherOption from = JsonData.from(json, WatcherOption.class);
        Assertions.assertTrue(from.isRealtime());
        Assertions.assertTrue(from.isPolling());
        Assertions.assertTrue(from.isFallbackPolling());
        Assertions.assertEquals(10, from.getLifetimeInSeconds());
        Assertions.assertEquals(TriggerType.CRON, from.getTriggerOption().getType());
        Assertions.assertEquals(-1, from.getTriggerOption().getRepeat());
        Assertions.assertEquals("* 0 * ? * * *", from.getTriggerOption().getExpression());
        Assertions.assertEquals("UTC+10", from.getTriggerOption().getTimezone());
    }

}
