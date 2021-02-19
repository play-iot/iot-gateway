package io.github.zero88.iot.connector.mqtt;

import io.github.zero88.iot.connector.mqtt.publisher.MqttPublisherConfig;
import io.github.zero88.iot.connector.mqtt.subscriber.MqttSubscriberConfig;
import io.github.zero88.qwe.CarlConfig.AppConfig;
import io.github.zero88.qwe.IConfig;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder(builderClassName = "Builder")
public final class MqttVerticleConfig implements IConfig {

    @Default
    private final MqttPublisherConfig publisher = MqttPublisherConfig.builder().build();
    @Default
    private final MqttSubscriberConfig subscriber = MqttSubscriberConfig.builder().build();

    @Override
    public String key() {
        return "__mqtt__";
    }

    @Override
    public Class<? extends IConfig> parent() {
        return AppConfig.class;
    }

}
