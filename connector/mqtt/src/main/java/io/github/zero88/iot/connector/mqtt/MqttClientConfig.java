package io.github.zero88.iot.connector.mqtt;

import java.util.concurrent.TimeUnit;

import io.github.zero88.utils.Strings;
import io.github.zero88.utils.UUID64;
import io.vertx.mqtt.MqttClientOptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder
public abstract class MqttClientConfig {

    public static final int RECONNECT_ATTEMPTS_INFINITE = -1;

    @NonNull
    protected abstract String clientPrefix();

    private String clientId;
    @Default
    private final boolean autoKeepAlive = true;
    /**
     * Keep alive interval
     *
     * @apiNote Default is {@code 30 seconds}
     */
    @Default
    private final int keepAliveInterval = MqttClientOptions.DEFAULT_KEEP_ALIVE_INTERVAL;
    /**
     * Keep alive time unit
     *
     * @apiNote Default is {@code SECONDS}
     * @see #keepAliveInterval
     */
    @Default
    private final TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
    /**
     * Reconnect attempts
     *
     * @apiNote Default is {@code 3 times}
     */
    @Default
    private final int reconnectAttempts = 3;
    /**
     * Reconnect interval
     *
     * @apiNote Default is {@code 10 seconds}
     * @see #reconnectTimeUnit
     */
    @Default
    private final int reconnectInterval = 10;
    /**
     * Reconnect time unit
     *
     * @apiNote Default is {@code SECONDS}
     */
    @Default
    private final TimeUnit reconnectTimeUnit = TimeUnit.SECONDS;

    @Default
    private final int connectTimeout = 5;
    @Default
    private final TimeUnit connectTimeoutTimeUnit = TimeUnit.SECONDS;

    /**
     * A flag that {@code MQTT client} request a {@code persistent session} when it connects to the {@code broker}
     *
     * @apiNote Default is {@code true}
     * @see MqttClientOptions#isCleanSession()
     * @see <a href="https://www.hivemq.com/blog/mqtt-essentials-part-7-persistent-session-queuing-messages/">Persistent
     *     session</a>
     */
    @Default
    private final boolean cleanSession = true;

    /**
     * @see MqttClientOptions#getAckTimeout()
     */
    @Default
    private final int ackTimeout = MqttClientOptions.DEFAULT_ACK_TIMEOUT;

    public String getClientId() {
        return this.clientId = Strings.fallback(this.clientId, () -> clientPrefix() + UUID64.random());
    }

    @JsonIgnore
    public boolean isEnableReconnect() {
        return reconnectAttempts > 0 || isInfinitiveReconnect();
    }

    @JsonIgnore
    public boolean isInfinitiveReconnect() {
        return reconnectAttempts == RECONNECT_ATTEMPTS_INFINITE;
    }

}
