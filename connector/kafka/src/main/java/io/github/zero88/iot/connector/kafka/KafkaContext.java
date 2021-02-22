package io.github.zero88.iot.connector.kafka;

import java.util.ArrayList;
import java.util.List;

import io.github.zero88.iot.connector.kafka.service.KafkaConsumerService;
import io.github.zero88.iot.connector.kafka.service.KafkaProducerService;
import io.github.zero88.qwe.component.ComponentContext;
import io.github.zero88.qwe.component.ComponentContext.DefaultComponentContext;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.reactivex.Completable;
import io.vertx.core.AsyncResult;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public final class KafkaContext extends DefaultComponentContext {

    private static final long DEFAULT_CLOSE_TIMEOUT_MS = 30 * 1000L;
    private KafkaConsumerService consumerService;
    private KafkaProducerService producerService;

    protected KafkaContext(@NonNull ComponentContext ctx) {
        super(ctx);
    }

    KafkaContext setup(SharedDataLocalProxy sharedData, KafkaConfig config, KafkaRouter router) {
        this.producerService = KafkaProducerService.create(sharedData, config.getProducerConfig(), router);
        this.consumerService = KafkaConsumerService.create(sharedData, config.getConsumerConfig(), router);
        return this;
    }

    Completable stop() {
        List<Completable> completables = new ArrayList<>();
        consumerService.consumers()
                       .parallelStream()
                       .forEach(
                           c -> c.close(event -> close(completables, (AsyncResult) event, KafkaClientType.CONSUMER)));
        producerService.producers()
                       .parallelStream()
                       .forEach(p -> p.close(DEFAULT_CLOSE_TIMEOUT_MS, event -> close(completables, (AsyncResult) event,
                                                                                      KafkaClientType.PRODUCER)));
        return Completable.merge(completables);
    }

    private void close(List<Completable> completables, AsyncResult event, KafkaClientType type) {
        if (event.failed()) {
            log.error("Failed when close Kafka {}", type, event.cause());
        }
        completables.add(Completable.complete());
    }

}
