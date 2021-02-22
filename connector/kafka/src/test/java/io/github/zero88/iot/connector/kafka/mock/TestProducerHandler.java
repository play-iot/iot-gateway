package io.github.zero88.iot.connector.kafka.mock;

import java.util.function.Consumer;

import io.github.zero88.iot.connector.kafka.handler.producer.AbstractKafkaProducerHandler;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.kafka.client.producer.RecordMetadata;

import lombok.Builder;
import lombok.NonNull;

@Builder(builderClassName = "Builder")
public class TestProducerHandler extends AbstractKafkaProducerHandler {

    @NonNull
    private final TestContext context;
    @NonNull
    private final Async async;
    @NonNull
    private final Consumer<Async> countdown;
    private final String topic;
    private final int partition;
    private final ErrorMessage errorMessage;

    private TestProducerHandler(@NonNull SharedDataLocalProxy sharedData, @NonNull TestContext context,
                                @NonNull Async async, @NonNull Consumer<Async> countdown, String topic, int partition,
                                ErrorMessage errorMessage) {
        super(sharedData);
        this.context = context;
        this.async = async;
        this.countdown = countdown;
        this.topic = topic;
        this.partition = partition;
        this.errorMessage = errorMessage;
    }

    @Override
    public void handleSuccess(RecordMetadata metadata) {
        try {
            context.assertNotNull(metadata);
            context.assertEquals(topic, metadata.getTopic());
            context.assertEquals(partition, metadata.getPartition());
        } finally {
            countdown.accept(async);
        }
    }

    @Override
    public void handleFailed(ErrorMessage message) {
        try {
            context.assertNotNull(message);
            context.assertEquals(errorMessage, message);
        } finally {
            countdown.accept(async);
        }
    }

    public static class Builder {

        public TestProducerHandler build(@NonNull SharedDataLocalProxy sharedData) {
            return new TestProducerHandler(sharedData, context, async, countdown, topic, partition, errorMessage);
        }

    }

}
