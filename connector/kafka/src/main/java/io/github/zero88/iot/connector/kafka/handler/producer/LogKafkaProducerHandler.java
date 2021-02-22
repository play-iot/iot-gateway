package io.github.zero88.iot.connector.kafka.handler.producer;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.vertx.kafka.client.producer.RecordMetadata;

/**
 * Responsible for logging metadata after sending Kafka record
 */
public final class LogKafkaProducerHandler extends AbstractKafkaProducerHandler {

    protected LogKafkaProducerHandler(SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    @Override
    public void handleSuccess(RecordMetadata metadata) {
        logger.info("Sent Kafka record successfully");
        logger.debug("Record metadata: {}", metadata.toJson());
    }

    @Override
    public void handleFailed(ErrorMessage message) {
        logger.error("Sent Kafka record failed: {}", message.toJson());
    }

}
