package io.github.zero88.iot.connector.kafka.mock;

import java.util.Collections;
import java.util.function.Supplier;

import io.github.zero88.iot.connector.kafka.KafkaConfig.ConsumerCfg;
import io.github.zero88.iot.connector.kafka.supplier.KafkaConsumerProvider;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventModel;
import io.github.zero88.qwe.event.EventbusClient;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockKafkaConsumer {

    private final Vertx vertx;
    private final ConsumerCfg consumerCfg;
    private final String topic;
    private final Supplier<EventModel> eventModelSupplier;
    private KafkaConsumer<String, EventMessage> consumer;

    public void start() {
        EventbusClient client = EventbusClient.create(vertx, MockKafkaConsumer.class.getName());
        consumer = KafkaConsumerProvider.create(vertx, consumerCfg, String.class, EventMessage.class);
        consumer.handler(record -> {
            System.err.println("CONSUMER Topic: " + record.topic());
            System.err.println(record.value().toJson().encodePrettily());
            EventModel eventModel = eventModelSupplier.get();
            client.fire(eventModel.getAddress(), eventModel.getPattern(), record.value());
        }).exceptionHandler(Throwable::printStackTrace);
        consumer.subscribe(Collections.singleton(topic));
    }

    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

}
