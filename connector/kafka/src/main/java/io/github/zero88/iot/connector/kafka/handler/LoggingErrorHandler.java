package io.github.zero88.iot.connector.kafka.handler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.github.zero88.iot.connector.kafka.ClientTechId;
import io.github.zero88.iot.connector.kafka.KafkaClientType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class LoggingErrorHandler<K, V> implements KafkaErrorHandler<K, V> {

    private final KafkaClientType type;

    @Override
    public void accept(ClientTechId<K, V> techId, String clientId, Throwable throwable) {
        log.error("Error in Kafka {} :: Client ID {} :: Technical ID {}", type, clientId, techId, throwable);
    }

}
