package io.github.zero88.iot.connector.mqtt.publisher;

import io.github.zero88.iot.connector.mqtt.AbstractQWEMqttClient;
import io.github.zero88.qwe.component.SharedDataLocalProxy;

import lombok.NonNull;

public class MqttPublisher extends AbstractQWEMqttClient<MqttPublisherConfig> {

    public MqttPublisher(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

}
