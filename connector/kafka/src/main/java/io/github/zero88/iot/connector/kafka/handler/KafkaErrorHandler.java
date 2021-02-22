package io.github.zero88.iot.connector.kafka.handler;

import io.github.zero88.iot.connector.kafka.ClientTechId;
import io.github.zero88.iot.connector.kafka.KafkaClientType;

public interface KafkaErrorHandler<K, V> {

    KafkaErrorHandler CONSUMER_ERROR_HANDLER = new LoggingErrorHandler(KafkaClientType.CONSUMER);
    KafkaErrorHandler PRODUCER_ERROR_HANDLER = new LoggingErrorHandler(KafkaClientType.PRODUCER);

    void accept(ClientTechId<K, V> techId, String clientId, Throwable throwable);

}
