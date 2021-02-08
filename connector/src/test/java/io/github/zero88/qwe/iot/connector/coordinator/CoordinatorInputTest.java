package io.github.zero88.qwe.iot.connector.coordinator;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.iot.connector.mock.MockSubject;
import io.github.zero88.qwe.iot.connector.mock.MockSubscriber;
import io.vertx.core.json.JsonObject;

public class CoordinatorInputTest {

    @Test
    public void test_serialize() throws JSONException {
        final CoordinatorInput<MockSubject> option = CoordinatorInput.<MockSubject>builder().subject(
            new MockSubject("m1")).subscriber(new MockSubscriber("s1")).build();
        final JsonObject expected = new JsonObject(
            "{\"subject\":{\"key\":\"m1\"},\"watcherOption\":{\"realtime\":true,\"lifetimeInSeconds\":-1," +
            "\"fallbackPolling\":true,\"polling\":false,\"triggerOption\":{\"type\":\"PERIODIC\"," +
            "\"intervalInSeconds\":5,\"repeat\":-1}},\"subscribers\":[{\"code\":\"s1\",\"type\":\"MOCK\",\"key" +
            "\":\"MOCK::s1\"}]}");
        JsonHelper.assertJson(expected, option.toJson());
    }

}
