package io.github.zero88.qwe.iot.connector.mock;

import io.github.zero88.qwe.iot.connector.subscriber.Subscriber;
import io.github.zero88.qwe.iot.connector.subscriber.SubscriberType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MockSubscriber implements Subscriber {

    private final String code;

    @Override
    public SubscriberType getType() {
        return SubscriberType.factory("mock");
    }

}
