package io.github.zero88.iot.connector.mqtt.publisher;

import io.github.zero88.iot.connector.mqtt.MqttClientConfig;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
public class MqttPublisherConfig extends MqttClientConfig {

    @Override
    protected @NonNull String clientPrefix() {
        return "qwe-mqtt-publisher-";
    }

}
