package io.github.zero88.iot.connector.mqtt.subscriber;

import io.github.zero88.iot.connector.mqtt.AbstractQWEMqttClient;
import io.github.zero88.iot.connector.mqtt.MqttConnectionAware;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.reactivex.Single;
import io.vertx.mqtt.MqttClient;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttSubscriber extends AbstractQWEMqttClient<MqttSubscriberConfig> {

    public MqttSubscriber(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    public Single<MqttClient> subscribe(@NonNull MqttSubscriberConfig config,
                                        @NonNull MqttConnectionAware connectionAware, @NonNull String topic,
                                        @NonNull MqttQoS qos) {
        return connect(config, connectionAware);
    }

}
