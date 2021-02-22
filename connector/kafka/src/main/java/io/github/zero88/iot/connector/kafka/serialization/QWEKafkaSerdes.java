package io.github.zero88.iot.connector.kafka.serialization;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import io.github.zero88.exceptions.ErrorCode;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.exceptions.CarlException;
import io.vertx.kafka.client.serialization.VertxSerdes;

import lombok.NonNull;

/**
 * Extend {@link VertxSerdes} for factory for creating serializers / deserializers
 *
 * @see VertxSerdes
 * @see Serdes
 */
public final class QWEKafkaSerdes extends Serdes {

    public static final class EventMessageSerde extends WrapperSerde<EventMessage> {

        EventMessageSerde() {
            super(new EventMessageSerializer(), new EventMessageDeserializer());
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> Serde<T> serdeFrom(@NonNull Class<T> type) {
        try {
            if (EventMessage.class.isAssignableFrom(type)) {
                return (Serde<T>) new EventMessageSerde();
            }
            return VertxSerdes.serdeFrom(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new CarlException(ErrorCode.INVALID_ARGUMENT, "Unsupported serialize/deserialize type", e);
        }
    }

}
