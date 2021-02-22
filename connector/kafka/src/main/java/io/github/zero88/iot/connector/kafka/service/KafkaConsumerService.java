package io.github.zero88.iot.connector.kafka.service;

import java.util.Collection;

import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import io.github.zero88.iot.connector.kafka.KafkaConfig.ConsumerCfg;
import io.github.zero88.iot.connector.kafka.KafkaRouter;

public interface KafkaConsumerService extends HasSharedData {

    static KafkaConsumerService create(SharedDataLocalProxy sharedData, ConsumerCfg config, KafkaRouter router) {
        return new ConsumerService(sharedData, config, router.getConsumerTechId(),
                                   router.getConsumerExceptionHandler()).create(router.getConsumerEvents());
    }

    <K, V> KafkaConsumer<K, V> consumer(String topic);

    Collection<KafkaConsumer> consumers();

}
