package io.github.zero88.qwe.iot.connector.bacnet.dto;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import io.github.zero88.qwe.JsonHelper;
import io.vertx.core.json.JsonObject;

class CovOutputTest {

    @Test
    void test_serialize() throws JSONException {
        final CovOutput cov = CovOutput.builder()
                                       .key("123")
                                       .cov(new JsonObject().put("a", 1).put("b", 2))
                                       .any(new JsonObject().put("xx", "ab"))
                                       .build();
        JsonHelper.assertJson(new JsonObject("{\"123\":{\"cov\":{\"a\":1,\"b\":2},\"xx\":\"ab\"}}"), cov.toJson());
    }

}
