package io.github.zero88.iot.connector.kafka.service;

import java.util.Collection;
import java.util.Map;

import io.github.zero88.iot.connector.kafka.KafkaConfig.ProducerCfg;
import io.github.zero88.iot.connector.kafka.KafkaRouter;
import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.transport.Transporter;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;

public interface KafkaProducerService extends Transporter, HasSharedData {

    static KafkaProducerService create(SharedDataLocalProxy sharedData, ProducerCfg config, KafkaRouter router) {
        return new ProducerService(sharedData, config, router.getProducerTechId(), router.getProducerExceptionHandler())
                   .create(router.getProducerEvents());
    }

    @Override
    default Vertx getVertx() {
        return sharedData().getVertx();
    }

    <K, V> KafkaProducer<K, V> producer(String topic);

    Collection<KafkaProducer> producers();

    default <V> void publish(String topic, V value) {
        this.publish(EventAction.CREATE, topic, value);
    }

    default <V> void publish(EventAction action, String topic, V value) {
        this.publish(action, topic, null, value);
    }

    default <K, V> void publish(String topic, K key, V value) {
        this.publish(EventAction.CREATE, topic, key, value);
    }

    default <K, V> void publish(EventAction action, String topic, K key, V value) {
        this.publish(action, topic, null, key, value);
    }

    default <K, V> void publish(String topic, Integer partition, K key, V value) {
        this.publish(EventAction.CREATE, topic, partition, key, value);
    }

    default <K, V> void publish(EventAction action, String topic, Integer partition, K key, V value) {
        this.publish(action, topic, partition, key, value, null);
    }

    default <K, V> void publish(EventAction action, String topic, Integer partition, K key, V value,
                                Map<String, Object> headers) {
        this.publish(EventMessage.initial(action), topic, partition, key, value, headers);
    }

    default <K, V> void publish(EventMessage message, String topic, Integer partition, K key, V value) {
        this.publish(message, topic, partition, key, value, null);
    }

    <K, V> void publish(EventMessage message, String topic, Integer partition, K key, V value,
                        Map<String, Object> headers);

}
