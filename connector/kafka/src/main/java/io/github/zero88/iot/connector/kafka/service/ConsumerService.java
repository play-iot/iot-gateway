package io.github.zero88.iot.connector.kafka.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zero88.iot.connector.kafka.ClientTechId;
import io.github.zero88.iot.connector.kafka.KafkaConfig.ConsumerCfg;
import io.github.zero88.iot.connector.kafka.KafkaEventMetadata;
import io.github.zero88.iot.connector.kafka.handler.KafkaErrorHandler;
import io.github.zero88.iot.connector.kafka.handler.consumer.ConsumerDispatcher;
import io.github.zero88.iot.connector.kafka.handler.consumer.KafkaConsumerHandler;
import io.github.zero88.iot.connector.kafka.supplier.KafkaConsumerProvider;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ConsumerService implements KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Getter
    @Accessors(fluent = true)
    private final SharedDataLocalProxy sharedData;
    private final ConsumerCfg config;
    private final Map<String, ClientTechId> techIdMap;
    private final Map<ClientTechId, KafkaErrorHandler> errorHandlers;
    private final Map<String, KafkaConsumer> consumers = new HashMap<>();

    public KafkaConsumer consumer(String topic) {
        return consumers.get(topic);
    }

    public Collection<KafkaConsumer> consumers() {
        return Collections.unmodifiableCollection(consumers.values());
    }

    ConsumerService create(Map<ClientTechId, Set<KafkaEventMetadata>> consumerEvents) {
        Map<ClientTechId, KafkaConsumer> temp = new HashMap<>();
        techIdMap.forEach((topic, techId) -> consumers.put(topic, temp.computeIfAbsent(techId, this::create)));
        consumerEvents.forEach((techId, metadata) -> registerHandler(temp.get(techId), metadata));
        logger.debug("Registered {} Kafka Consumer(s) successfully", temp.size());
        return this;
    }

    private <K, V> void registerHandler(KafkaConsumer<K, V> consumer, Set<KafkaEventMetadata> identicalTechIdEvents) {
        ConsumerDispatcher<K, V> dispatcher = createDispatcher(identicalTechIdEvents);
        consumer.handler(dispatcher::accept).subscribe(dispatcher.topics());
    }

    private <K, V> KafkaConsumer<K, V> create(ClientTechId techId) {
        KafkaConsumer consumer = KafkaConsumerProvider.create(sharedData().getVertx(), config,
                                                              techId.getKeySerdes().deserializer(),
                                                              techId.getValueSerdes().deserializer());
        return consumer.exceptionHandler(
            t -> errorHandlers.getOrDefault(techId, KafkaErrorHandler.CONSUMER_ERROR_HANDLER)
                              .accept(techId, config.getClientId(), (Throwable) t));
    }

    private <K, V> ConsumerDispatcher<K, V> createDispatcher(Set<KafkaEventMetadata> identicalTechIdEvents) {
        ConsumerDispatcher.Builder<K, V> builder = ConsumerDispatcher.builder();
        identicalTechIdEvents.forEach(event -> {
            KafkaConsumerHandler handler = createConsumerHandler(event);
            builder.handler(event.getTopic(), handler);
            logger.info("Registering Kafka Consumer | Topic: {} | Kind: {} | Event: {} - {} | Handler: {} | " +
                        "Transformer: {}", event.getTopic(), event.getTechId().toString(),
                        event.getEventModel().getAddress(), event.getEventModel().getPattern(),
                        handler.getClass().getName(), handler.transformer().getClass().getName());
        });
        return builder.build();
    }

    private KafkaConsumerHandler createConsumerHandler(KafkaEventMetadata metadata) {
        KafkaConsumerHandler consumerHandler = metadata.getConsumerHandler();
        if (Objects.isNull(consumerHandler)) {
            consumerHandler = KafkaConsumerHandler.createBroadcaster(sharedData(), metadata.getEventModel());
        }
        return consumerHandler.register(metadata.getTransformer());
    }

}
