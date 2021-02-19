package io.github.zero88.iot.connector.mqtt.cache;

import io.github.zero88.iot.connector.mqtt.publisher.MqttPublisher;
import io.github.zero88.iot.connector.mqtt.subscriber.MqttSubscriber;
import io.github.zero88.qwe.cache.CacheInitializer;
import io.github.zero88.qwe.component.SharedDataLocalProxy;

public final class MqttCacheInitializer implements CacheInitializer {

    public static final String SUBSCRIBERS_CACHE = "QWE_MQTT_SUBSCRIBERS";
    public static final String PUBLISHERS_CACHE = "QWE_MQTT_PUBLISHERS";

    @Override
    public void init(SharedDataLocalProxy context) {
        context.addData(SUBSCRIBERS_CACHE, new MqttClientRegistry<>(MqttSubscriber.class));
        context.addData(PUBLISHERS_CACHE, new MqttClientRegistry<>(MqttPublisher.class));
    }

}
