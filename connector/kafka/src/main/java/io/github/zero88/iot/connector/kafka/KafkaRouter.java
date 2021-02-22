package io.github.zero88.iot.connector.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.github.zero88.iot.connector.kafka.handler.KafkaErrorHandler;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.ErrorCode;
import io.github.zero88.utils.Strings;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Register metadata to init Kafka client that lives in the end of application lifetime
 */
@Getter(AccessLevel.PUBLIC)
public final class KafkaRouter {

    private final Map<String, ClientTechId> consumerTechId = new HashMap<>();
    private final Map<ClientTechId, Set<KafkaEventMetadata>> consumerEvents = new HashMap<>();

    private final Map<String, ClientTechId> producerTechId = new HashMap<>();
    private final Set<KafkaEventMetadata> producerEvents = new HashSet<>();

    private final Map<ClientTechId, KafkaErrorHandler> consumerExceptionHandler = new HashMap<>();
    private final Map<ClientTechId, KafkaErrorHandler> producerExceptionHandler = new HashMap<>();

    public KafkaRouter registerKafkaEvent(KafkaEventMetadata... kafkaEvents) {
        Arrays.stream(kafkaEvents).filter(Objects::nonNull).forEach(this::registerKafkaEvent);
        return this;
    }

    public KafkaRouter registerKafkaEvent(@NonNull KafkaEventMetadata kafkaEvent) {
        ClientTechId techId = kafkaEvent.getTechId();
        String topic = kafkaEvent.getTopic();
        if (kafkaEvent.getType() == KafkaClientType.CONSUMER) {
            validate(techId, topic, consumerTechId);
            this.consumerEvents.computeIfAbsent(techId, id -> new HashSet<>()).add(kafkaEvent);
        }
        if (kafkaEvent.getType() == KafkaClientType.PRODUCER) {
            validate(techId, topic, producerTechId);
            this.producerEvents.add(kafkaEvent);
        }
        return this;
    }

    private void validate(ClientTechId techId, String topic, Map<String, ClientTechId> techIdMap) {
        ClientTechId existedId = techIdMap.get(topic);
        if (Objects.nonNull(existedId) && !existedId.equals(techId)) {
            throw new CarlException(ErrorCode.INVALID_ARGUMENT, Strings.format(
                "Topic {0} is already registered with another pair classes: {1} - {2}", topic,
                existedId.getKeyClass().getName(), existedId.getValueClass().getName()));
        }
        techIdMap.put(topic, techId);
    }

    public KafkaRouter addConsumerErrorHandler(@NonNull ClientTechId techId,
                                               @NonNull KafkaErrorHandler throwableHandler) {
        this.consumerExceptionHandler.put(techId, throwableHandler);
        return this;
    }

    public KafkaRouter addProducerErrorHandler(@NonNull ClientTechId techId,
                                               @NonNull KafkaErrorHandler throwableHandler) {
        this.producerExceptionHandler.put(techId, throwableHandler);
        return this;
    }

}
