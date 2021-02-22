package io.github.zero88.iot.connector.kafka.mock;

import java.util.function.Supplier;

import org.apache.kafka.clients.producer.ProducerRecord;

import io.github.zero88.iot.connector.kafka.KafkaConfig.ProducerCfg;
import io.github.zero88.iot.connector.kafka.supplier.KafkaProducerSupplier;
import io.github.zero88.qwe.event.EventMessage;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockKafkaProducer {

    private final Vertx vertx;
    private final ProducerCfg producerCfg;
    private final String topic;
    private final Supplier<EventMessage> messageSupplier;
    private KafkaProducer<String, EventMessage> producer;

    public void start() {
        producer = KafkaProducerSupplier.create(vertx, producerCfg, String.class, EventMessage.class);
        vertx.setPeriodic(2000, id -> {
            EventMessage message = messageSupplier.get();
            System.err.println("PRODUCER ID: " + id);
            System.err.println(message.toJson().encodePrettily());
            producer.unwrap().send(new ProducerRecord<>(topic, message));
        });
    }

    public void stop() {
        if (producer != null) {
            producer.close();
        }
    }

}
