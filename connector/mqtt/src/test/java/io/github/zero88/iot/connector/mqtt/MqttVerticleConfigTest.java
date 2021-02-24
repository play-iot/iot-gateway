package io.github.zero88.iot.connector.mqtt;

import org.junit.jupiter.api.Test;

class MqttVerticleConfigTest {

    @Test
    void serialize() {
        System.out.println(MqttVerticleConfig.builder().build().toJson());
    }

}
