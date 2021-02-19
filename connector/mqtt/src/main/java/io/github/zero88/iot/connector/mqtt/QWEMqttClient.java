package io.github.zero88.iot.connector.mqtt;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.zero88.qwe.auth.Certificate;
import io.github.zero88.qwe.auth.Certificate.CertificateType;
import io.github.zero88.qwe.auth.Credential;
import io.github.zero88.qwe.auth.Credential.CredentialType;
import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.utils.Strings;
import io.reactivex.Single;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import lombok.NonNull;

public interface QWEMqttClient<T extends MqttClientConfig> extends HasSharedData {

    /**
     * Check whether {@code MQTT client} is in {@code persistent session}
     *
     * @return {@code true} if {@code MQTT client} is in {@code persistent session}
     * @see MqttClientOptions#isCleanSession()
     */
    boolean isInSession();

    /**
     * Check whether MQTT client is still connecting to MQTT broker/server
     *
     * @return {@code true} if {@code MQTT client} is connected
     */
    boolean isConnected();

    @NonNull Single<MqttClient> connect(@NonNull T config, @NonNull MqttConnectionAware connectionAware);

    static MqttClientOptions createMqttOption(@NonNull MqttClientConfig config,
                                              @NonNull MqttConnectionAware connectionAware) {
        final long keepAliveInterval = TimeUnit.SECONDS.convert(config.getKeepAliveInterval(),
                                                                config.getKeepAliveTimeUnit());
        final long connectTimeout = TimeUnit.MILLISECONDS.convert(config.getConnectTimeout(),
                                                                  config.getConnectTimeoutTimeUnit());
        final long ackTimeOut = config.getAckTimeout();
        final MqttClientOptions options = new MqttClientOptions().setClientId(config.getClientId())
                                                                 .setAutoKeepAlive(config.isAutoKeepAlive())
                                                                 .setKeepAliveInterval((int) keepAliveInterval)
                                                                 .setAckTimeout((int) ackTimeOut)
                                                                 .setSsl(connectionAware.useSSL());
        options.setConnectTimeout((int) connectTimeout);
        final Credential credential = connectionAware.credential();
        if (Objects.nonNull(credential)) {
            if (credential.getType() != CredentialType.BASIC && credential.getType() != CredentialType.TOKEN) {
                throw new CarlException("Unsupported credential type [" + credential.getType() + "]");
            }
            if (Strings.isBlank(credential.getUser())) {
                options.setUsername(credential.secretValue());
            } else {
                options.setUsername(credential.getUser()).setPassword(credential.secretValue());
            }
        }
        final Certificate cert = connectionAware.certificate();
        if (Objects.nonNull(cert)) {
            if (cert.getType() == CertificateType.JKS) {
                options.setKeyStoreOptions((JksOptions) cert.toKeyCert());
            }
            if (cert.getType() == CertificateType.PKCS12) {
                options.setPfxKeyCertOptions((PfxOptions) cert.toKeyCert());
            }
            if (cert.getType() == CertificateType.PKCS1 || cert.getType() == CertificateType.PKCS8) {
                options.setPemKeyCertOptions((PemKeyCertOptions) cert.toKeyCert());
            }
            if (cert.getType() == CertificateType.PEM_CERT) {
                options.setPemTrustOptions((PemTrustOptions) cert.toTrustCert());
            }
        }
        return options;
    }

}
