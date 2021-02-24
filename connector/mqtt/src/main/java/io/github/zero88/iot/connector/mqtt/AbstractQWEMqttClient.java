package io.github.zero88.iot.connector.mqtt;

import java.net.ConnectException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.CommunicationProtocolException;
import io.github.zero88.qwe.exceptions.ConflictException;
import io.github.zero88.qwe.exceptions.NetworkException;
import io.github.zero88.qwe.exceptions.SecurityException.AuthenticationException;
import io.github.zero88.qwe.exceptions.SecurityException.InsufficientPermissionError;
import io.github.zero88.qwe.exceptions.TimeoutException;
import io.github.zero88.qwe.protocol.ConnectStrategy;
import io.github.zero88.scheduler.core.PeriodicTaskExecutor;
import io.github.zero88.scheduler.core.Task;
import io.github.zero88.scheduler.core.TaskExecutionContext;
import io.github.zero88.scheduler.core.TaskExecutorMonitor.TaskExecutorNoMonitor;
import io.github.zero88.scheduler.core.TaskResult;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractQWEMqttClient<T extends MqttClientConfig> implements QWEMqttClient<T> {

    public static final String THREAD_NAME = "mqtt.client-connection-thread";
    private final Logger log = LoggerFactory.getLogger(getClass());
    @NonNull
    @Accessors(fluent = true)
    private final SharedDataLocalProxy sharedData;
    private MqttClient mqttClient;
    private boolean inSession;

    @Override
    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }

    @Override
    public Single<MqttClient> connect(@NonNull T config, @NonNull MqttConnectionAware connectionAware) {
        return doConnect(QWEMqttClient.createMqttOption(config, connectionAware), config, connectionAware);
    }

    protected Single<MqttClient> doConnect(@NonNull MqttClientOptions options, @NonNull T config,
                                           @NonNull MqttConnectionAware connAware) {
        final AsyncConnectTask task = AsyncConnectTask.builder()
                                                      .vertx(sharedData().getVertx())
                                                      .options(options)
                                                      .connectionAware(connAware)
                                                      .build();
        final int repeat = !config.isEnableReconnect() || connAware.strategy() == ConnectStrategy.FAILED_FAST
                           ? 1
                           : config.getReconnectAttempts();
        PeriodicTaskExecutor.builder()
                            .vertx(sharedData().getVertx())
                            .interval(config.getReconnectInterval())
                            .timeUnit(config.getReconnectTimeUnit())
                            .repeat(repeat)
                            .task(task)
                            .monitor(task)
                            .build()
                            .start(sharedData().getVertx().createSharedWorkerExecutor(THREAD_NAME, 3));
        return task.client().map(client -> {
            this.mqttClient = client.getKey();
            this.inSession = client.getValue();
            return this.mqttClient;
        });
    }

    @Builder
    public static class AsyncConnectTask implements Task, TaskExecutorNoMonitor {

        private final Vertx vertx;
        private final MqttClientOptions options;
        private final MqttConnectionAware connectionAware;
        private final SingleSubject<Entry<MqttClient, Boolean>> subject = SingleSubject.create();

        @Override
        public boolean isAsync() {
            return true;
        }

        /**
         * @param executionContext executor
         * @see
         * <a href="https://github.com/vert-x3/vertx-mqtt/blob/master/src/main/java/io/vertx/mqtt/impl/MqttClientImpl.java#L218-L221">bug</a>
         */
        @Override
        public void execute(@NonNull TaskExecutionContext executionContext) {
            //TODO workaround because cannot reuse mqtt-client after vertx mqtt client failed to connect, it doesn't
            // release private status
            final MqttClient client = MqttClient.create(vertx, options);
            client.connect(connectionAware.port(), connectionAware.host())
                  .map(ackMessage -> new SimpleEntry<>(client, handleAckConnect(ackMessage)))
                  .onSuccess(result -> {
                      boolean f = executionContext.promise().tryComplete(result);
                      System.out.println("COMPLETED :" + f);
                      executionContext.forceStopExecution();
                  })
                  .onFailure(t -> executionContext.promise().fail(handleConnectionError(t)));
        }

        public Single<Entry<MqttClient, Boolean>> client() {
            return subject;
        }

        protected boolean handleAckConnect(@NonNull MqttConnAckMessage ack) {
            if (ack.code() == MqttConnectReturnCode.CONNECTION_ACCEPTED) {
                return ack.isSessionPresent();
            }
            if (ack.code() == MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED ||
                ack.code() == MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION) {
                throw new ConflictException(
                    "MQTT [" + connectionAware.address() + "] refuses connection due to " + ack.code().name());
            }
            if (ack.code() == MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD) {
                throw new AuthenticationException(
                    "MQTT [" + connectionAware.address() + "] refuses connection due to invalid username/password");
            }
            if (ack.code() == MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED) {
                throw new InsufficientPermissionError(
                    "MQTT [" + connectionAware.address() + "] refuses connection due to not authorized");
            }
            if (ack.code() == MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE) {
                throw new NetworkException("MQTT address [" + connectionAware.address() + "] is unavailable");
            }
            throw new CommunicationProtocolException(
                "MQTT [" + connectionAware.address() + "] refuses connection due to unknown error [" + ack.code() +
                "]");
        }

        protected CarlException handleConnectionError(@NonNull Throwable error) {
            if (error instanceof ConnectTimeoutException) {
                return new TimeoutException("MQTT address [" + connectionAware.address() + "] cannot establish on time",
                                            error);
            }
            if (error instanceof ConnectException) {
                return new NetworkException("MQTT address [" + connectionAware.address() + "] is unreachable", error);
            }
            if (error instanceof CarlException) {
                return (CarlException) error;
            }
            return new NetworkException("Unable connect to MQTT address [" + connectionAware.address() + "]", error);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onCompleted(@NonNull TaskResult data) {
            final Throwable throwable = data.getError();
            if (Objects.nonNull(throwable)) {
                subject.onError(throwable);
            } else {
                subject.onSuccess((Entry<MqttClient, Boolean>) data.getData());
            }
        }

    }

}
