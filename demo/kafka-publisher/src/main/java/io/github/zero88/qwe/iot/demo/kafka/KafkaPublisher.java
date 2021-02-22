package io.github.zero88.qwe.iot.demo.kafka;

import java.util.UUID;

import io.github.zero88.iot.connector.kafka.KafkaContext;
import io.github.zero88.iot.connector.kafka.KafkaEventMetadata;
import io.github.zero88.iot.connector.kafka.KafkaProvider;
import io.github.zero88.iot.connector.kafka.KafkaRouter;
import io.github.zero88.iot.connector.kafka.service.KafkaProducerService;
import io.github.zero88.qwe.component.ApplicationVerticle;
import io.github.zero88.qwe.component.ContextLookup;
import io.github.zero88.qwe.event.EventAction;

public final class KafkaPublisher extends ApplicationVerticle {

    private static final String TOPIC = "GPIO";

    @Override
    public void start() {
        super.start();
        this.addProvider(new KafkaProvider(initKafkaRouter()));
    }

    @Override
    public void onInstallCompleted(ContextLookup lookup) {
        startProducer(lookup.query(KafkaContext.class));
    }

    private void startProducer(KafkaContext context) {
        logger.info("Starting Producer Service...");
        KafkaProducerService producerService = context.getProducerService();
        vertx.setPeriodic(3000, id -> {
            logger.info("Sending data...");
            producerService.publish(EventAction.CREATE, TOPIC, 0, this.deploymentID(), UUID.randomUUID());
        });
    }

    private KafkaRouter initKafkaRouter() {
        return new KafkaRouter().registerKafkaEvent(
            KafkaEventMetadata.producer().topic(TOPIC).keyClass(String.class).valueClass(UUID.class).build());
    }

}
