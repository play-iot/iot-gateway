package io.github.zero88.iot.connector.kafka;

import io.github.zero88.qwe.component.ComponentProvider;
import io.github.zero88.qwe.component.SharedDataLocalProxy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class KafkaProvider implements ComponentProvider<KafkaVerticle> {

    private final KafkaRouter kafkaRouter;

    @Override
    public Class<KafkaVerticle> componentClass() {
        return KafkaVerticle.class;
    }

    @Override
    public KafkaVerticle provide(SharedDataLocalProxy sharedDataLocalProxy) {
        return new KafkaVerticle(sharedDataLocalProxy, kafkaRouter);
    }

}
