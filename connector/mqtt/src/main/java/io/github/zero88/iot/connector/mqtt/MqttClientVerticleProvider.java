package io.github.zero88.iot.connector.mqtt;

import io.github.zero88.qwe.component.ComponentProvider;
import io.github.zero88.qwe.component.SharedDataLocalProxy;

public final class MqttClientVerticleProvider implements ComponentProvider<MqttClientVerticle> {

    @Override
    public Class<MqttClientVerticle> componentClass() {
        return MqttClientVerticle.class;
    }

    @Override
    public MqttClientVerticle provide(SharedDataLocalProxy sharedDataLocalProxy) {
        return new MqttClientVerticle(sharedDataLocalProxy);
    }

}
