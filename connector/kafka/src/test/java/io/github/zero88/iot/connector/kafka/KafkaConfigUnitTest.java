package io.github.zero88.iot.connector.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.zero88.iot.connector.kafka.supplier.KafkaConsumerProvider;
import io.github.zero88.iot.connector.kafka.supplier.KafkaProducerSupplier;
import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.utils.Configs;
import io.vertx.ext.unit.junit.VertxUnitRunner;


@RunWith(VertxUnitRunner.class)
public class KafkaConfigUnitTest extends KafkaVerticleTestBase {

    @Test
    public void test_can_create_consumer() {
        KafkaConfig from = IConfig.from(Configs.loadJsonConfig("kafka.json"), KafkaConfig.class);
        KafkaConsumerProvider.create(vertx, from.getConsumerConfig(), String.class, EventMessage.class).close();
    }

    @Test
    public void test_can_create_producer() {
        KafkaConfig from = IConfig.from(Configs.loadJsonConfig("kafka.json"), KafkaConfig.class);
        KafkaProducerSupplier.create(vertx, from.getProducerConfig(), String.class, EventMessage.class).close();
    }

}
