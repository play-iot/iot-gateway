package io.github.zero88.qwe.iot.service.bacnet.websocket;

import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventModel;
import io.github.zero88.qwe.event.EventPattern;
import io.github.zero88.qwe.iot.connector.subscriber.Subscriber;
import io.github.zero88.qwe.iot.connector.subscriber.SubscriberType;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class WebSocketCOVSubscriber implements Subscriber {

    @Default
    private final String wsPath = "/cov";
    @Default
    private final String publishAddress = "bacnet.websocket.cov";
    @Default
    private final EventAction action = EventAction.MONITOR;

    @Override
    public SubscriberType getType() {
        return SubscriberType.factory("websocket_server");
    }

    @Override
    public String getCode() {
        return "bacnet_cov";
    }

    public EventModel toEventModel() {
        return EventModel.builder()
                         .address(publishAddress)
                         .event(action)
                         .pattern(EventPattern.PUBLISH_SUBSCRIBE)
                         .build();
    }

}
