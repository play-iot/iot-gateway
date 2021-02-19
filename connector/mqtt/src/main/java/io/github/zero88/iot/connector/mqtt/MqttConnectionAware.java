package io.github.zero88.iot.connector.mqtt;

import java.util.Objects;

import io.github.zero88.qwe.auth.Certificate;
import io.github.zero88.qwe.auth.Credential;
import io.github.zero88.qwe.protocol.ConnectStrategy;
import io.github.zero88.qwe.protocol.ConnectionAware;
import io.github.zero88.qwe.protocol.Protocol;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(fluent = true)
public final class MqttConnectionAware implements ConnectionAware {

    @NonNull
    private final String host;
    private Integer port;
    @Default
    private final ConnectStrategy strategy = ConnectStrategy.FAILED_FAST;
    @Default
    private final boolean useSSL = false;
    private final Credential credential;
    private final Certificate certificate;

    public int port() {
        if (Objects.isNull(port)) {
            return port = useSSL() && Objects.isNull(port) ? 8883 : 1883;
        }
        return port;
    }

    @Override
    public @NonNull Protocol protocol() {
        return Protocol.MQTT;
    }

    @Override
    public @NonNull String address() {
        String schema = useSSL() ? "mqtts" : "mqtt";
        return schema + "://" + host() + ":" + port();
    }

}
