package io.github.zero88.iot.connector.mqtt;

import io.github.zero88.iot.connector.mqtt.cache.MqttCacheInitializer;
import io.github.zero88.qwe.component.ComponentContext;
import io.github.zero88.qwe.component.ComponentVerticle;
import io.github.zero88.qwe.component.SharedDataLocalProxy;

import lombok.NonNull;

public final class MqttClientVerticle extends ComponentVerticle<MqttVerticleConfig, ComponentContext> {

    MqttClientVerticle(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    @Override
    public @NonNull Class<MqttVerticleConfig> configClass() {
        return MqttVerticleConfig.class;
    }

    @Override
    public @NonNull String configFile() {
        return "mqtt.json";
    }

    @Override
    public void start() {
        super.start();
        new MqttCacheInitializer().init(sharedData());
    }

}
