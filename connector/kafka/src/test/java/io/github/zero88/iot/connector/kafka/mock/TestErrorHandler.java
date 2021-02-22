package io.github.zero88.iot.connector.kafka.mock;

import java.util.function.Consumer;

import io.github.zero88.iot.connector.kafka.ClientTechId;
import io.github.zero88.iot.connector.kafka.handler.KafkaErrorHandler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestErrorHandler<K, V> implements KafkaErrorHandler<K, V> {

    @NonNull
    private final TestContext context;
    @NonNull
    private final Async async;
    @NonNull
    private final Consumer<Async> countdown;

    @Override
    public void accept(ClientTechId<K, V> techId, String clientId, Throwable throwable) {
        try {
            System.err.println("TechId " + techId + " - ClientId " + clientId);
            context.fail(throwable);
        } finally {
            countdown.accept(async);
        }
    }

}
