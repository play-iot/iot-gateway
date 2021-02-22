package io.github.zero88.iot.connector.kafka.handler.consumer;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventModel;
import io.github.zero88.qwe.event.EventPattern;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.ErrorCode;
import io.github.zero88.utils.Strings;

import lombok.NonNull;

/**
 * Responsible for broadcasting data via {@code eventbus} after receiving data from {@code Kafka Consumer}
 *
 * @see AbstractKafkaConsumerHandler
 * @see EventMessage
 * @see EventbusClient
 */
public final class KafkaBroadcaster<K, V, T extends KafkaBroadcasterTransformer<K, V>>
    extends AbstractKafkaConsumerHandler<K, V, T, EventMessage> {

    private final EventModel model;

    @SuppressWarnings("unchecked")
    KafkaBroadcaster(SharedDataLocalProxy sharedData, @NonNull EventModel model) {
        super(sharedData);
        if (model.getPattern() == EventPattern.REQUEST_RESPONSE) {
            throw new CarlException(ErrorCode.INVALID_ARGUMENT,
                                    Strings.format("Does not supported {0} in {1}", EventPattern.REQUEST_RESPONSE,
                                                   KafkaBroadcaster.class.getSimpleName()));
        }
        this.model = model;
        register((T) KafkaBroadcasterTransformer.DEFAULT);
    }

    @Override
    public void execute(EventMessage result) {
        EventbusClient controller = EventbusClient.create(getVertx());
        controller.fire(model.getAddress(), model.getPattern(), result);
    }

}
