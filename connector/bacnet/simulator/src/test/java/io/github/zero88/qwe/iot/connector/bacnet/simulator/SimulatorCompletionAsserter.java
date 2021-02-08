package io.github.zero88.qwe.iot.connector.bacnet.simulator;

import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.dto.ErrorData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.iot.connector.bacnet.handler.DiscoverCompletionHandler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SimulatorCompletionAsserter extends DiscoverCompletionHandler {

    @NonNull
    private final TestContext context;
    @NonNull
    private final Async async;
    @NonNull
    private final JsonObject expected;

    @Override
    public boolean success(@NonNull RequestData requestData) {
        JsonHelper.assertJson(context, async, expected, requestData.body());
        return true;
    }

    @Override
    public boolean error(@NonNull ErrorData error) {
        JsonHelper.assertJson(context, async, expected, error.toJson());
        return true;
    }

}
