package io.github.zero88.iot.connector.mqtt.cache;

import io.github.zero88.qwe.cache.AbstractLocalCache;

import io.github.zero88.iot.connector.mqtt.QWEMqttClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class MqttClientRegistry<T extends QWEMqttClient> extends AbstractLocalCache<String, T, MqttClientRegistry<T>> {

    private final Class<T> mqttClientType;

    @Override
    protected @NonNull String keyLabel() {
        return "Client Id";
    }

    @Override
    protected @NonNull String valueLabel() {
        return mqttClientType.getSimpleName();
    }

}
