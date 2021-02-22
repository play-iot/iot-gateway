package io.github.zero88.qwe.iot.demo.kafka;

import java.util.UUID;

import io.github.zero88.iot.connector.kafka.KafkaContext;
import io.github.zero88.iot.connector.kafka.KafkaEventMetadata;
import io.github.zero88.iot.connector.kafka.KafkaProvider;
import io.github.zero88.iot.connector.kafka.KafkaRouter;
import io.github.zero88.iot.connector.kafka.service.KafkaConsumerService;
import io.github.zero88.qwe.component.ApplicationVerticle;
import io.github.zero88.qwe.component.ContextLookup;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventModel;
import io.github.zero88.qwe.event.EventPattern;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.http.event.WebSocketServerEventMetadata;
import io.github.zero88.qwe.http.server.HttpServerProvider;
import io.github.zero88.qwe.http.server.HttpServerRouter;

public final class KafkaConsumerDashboard extends ApplicationVerticle {

    public static final EventModel KAFKA_EB_PUBLISHER = EventModel.builder()
                                                                  .address("edge.kafka.data")
                                                                  .pattern(EventPattern.PUBLISH_SUBSCRIBE)
                                                                  .local(false)
                                                                  .event(EventAction.GET_ONE)
                                                                  .build();
    public static final EventModel KAFKA_ENABLED = EventModel.builder()
                                                             .address("edge.kafka.manage.enable")
                                                             .pattern(EventPattern.REQUEST_RESPONSE)
                                                             .event(EventAction.UPDATE)
                                                             .local(true)
                                                             .build();

    private KafkaConsumerService consumerService;

    @Override
    public void start() {
        logger.info("DASHBOARD KAFKA DEMO");
        super.start();
        this.addProvider(new HttpServerProvider(initHttpRouter()))
            .addProvider(new KafkaProvider(initKafkaRouter()));
    }

    @Override
    public void registerEventbus(EventbusClient eventClient) {
        eventClient.register(KAFKA_ENABLED, new EnableKafkaEventListener());
    }

    @Override
    public void onInstallCompleted(ContextLookup lookup) {
        super.onInstallCompleted(lookup);
    }

    private KafkaRouter initKafkaRouter() {
        return new KafkaRouter().registerKafkaEvent(KafkaEventMetadata.consumer()
                                                                      .model(KAFKA_EB_PUBLISHER)
                                                                      .topic("GPIO")
                                                                      .keyClass(String.class)
                                                                      .valueClass(UUID.class)
                                                                      .build());
    }

    private HttpServerRouter initHttpRouter() {
        return new HttpServerRouter().registerEventBusApi(EnableKafkaProducerApi.class)
                                     .registerEventBusSocket(WebSocketServerEventMetadata.create(KAFKA_EB_PUBLISHER));
    }

    private void startConsumer(KafkaContext context) {
        consumerService = context.getConsumerService();
    }

}
