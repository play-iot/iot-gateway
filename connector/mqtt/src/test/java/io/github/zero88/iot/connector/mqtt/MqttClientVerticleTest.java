package io.github.zero88.iot.connector.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;

import io.github.zero88.iot.connector.mqtt.subscriber.MqttSubscriber;
import io.github.zero88.iot.connector.mqtt.subscriber.MqttSubscriberConfig;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.auth.certificate.PEMCert;
import io.github.zero88.qwe.auth.credential.BasicCredential;
import io.github.zero88.qwe.component.ComponentTestHelper;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.exceptions.NetworkException;
import io.github.zero88.qwe.exceptions.TimeoutException;
import io.github.zero88.qwe.protocol.ConnectStrategy;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.NonNull;
import lombok.SneakyThrows;

@ExtendWith(VertxExtension.class)
class MqttClientVerticleTest {

    private static final String MOSQUITTO_HOST = "test.mosquitto.org";
    private static final String MOSQUITTO_CERT = "mosquitto.org.crt";
    private static final String EMQX_HOST = "broker.emqx.io";
    private static final String EMQX_CERT = "broker.emqx.io-ca.crt";

    private SharedDataLocalProxy sharedData;

    @BeforeAll
    static void beforeAll() {
        TestHelper.setup();
        ((Logger) LoggerFactory.getLogger("io.vertx.mqtt")).setLevel(Level.DEBUG);
    }

    @BeforeEach
    void beforeEach(Vertx vertx) {
        sharedData = ComponentTestHelper.createSharedData(vertx, MqttClientVerticle.class);
    }

    @Test
    void test_connect_unreal_host_in_background(VertxTestContext testContext) {
        final MqttConnectionAware connAware = MqttConnectionAware.builder()
                                                                 .host("localhost")
                                                                 .strategy(ConnectStrategy.BACKGROUND)
                                                                 .build();
        final MqttSubscriberConfig config = MqttSubscriberConfig.builder()
                                                                .reconnectAttempts(2)
                                                                .reconnectInterval(2)
                                                                .build();
        new MqttSubscriber(sharedData).connect(config, connAware)
                                      .subscribe(c -> testContext.failNow("Must error"),
                                                 err -> testContext.verify(() -> {
                                                     Assertions.assertTrue(err instanceof NetworkException);
                                                     Assertions.assertEquals(
                                                         "MQTT address [mqtt://localhost:1883] is unreachable",
                                                         err.getMessage());
                                                     testContext.completeNow();
                                                 }));
    }

    @Test
    void test_connect_not_mqtt_host_in_background(VertxTestContext testContext) {
        final MqttConnectionAware connAware = MqttConnectionAware.builder()
                                                                 .host("hivemq.com")
                                                                 .strategy(ConnectStrategy.BACKGROUND)
                                                                 .build();
        final MqttSubscriberConfig subscriberConfig = MqttSubscriberConfig.builder()
                                                                          .reconnectAttempts(2)
                                                                          .reconnectInterval(2)
                                                                          .build();
        new MqttSubscriber(sharedData).connect(subscriberConfig, connAware)
                                      .subscribe(c -> testContext.failNow("Must error"),
                                                 err -> testContext.verify(() -> {
                                                     Assertions.assertTrue(err instanceof TimeoutException);
                                                     Assertions.assertEquals(
                                                         "MQTT address [mqtt://hivemq.com:1883] cannot establish on " +
                                                         "time", err.getMessage());
                                                     testContext.completeNow();
                                                 }));
    }

    @Test
    void test_connect_emqx_without_ssl_success(VertxTestContext testContext) {
        final MqttConnectionAware connAware = MqttConnectionAware.builder().host(EMQX_HOST).build();
        new MqttSubscriber(sharedData).connect(MqttSubscriberConfig.builder().build(), connAware)
                                      .subscribe(client -> testContext.verify(() -> {
                                          Assertions.assertTrue(client.isConnected());
                                          Assertions.assertTrue(client.clientId().startsWith("qwe-mqtt-"));
                                          testContext.completeNow();
                                      }), testContext::failNow);
    }

    @Test
    void test_connect_mosquitto_ssl_success(VertxTestContext testContext) throws IOException {
        final PEMCert cert = PEMCert.builder().certValue(readCert(MOSQUITTO_CERT)).build();
        final MqttConnectionAware connAware = MqttConnectionAware.builder()
                                                                 .host(MOSQUITTO_HOST)
                                                                 .useSSL(true)
                                                                 .certificate(cert)
                                                                 .build();
        new MqttSubscriber(sharedData).connect(MqttSubscriberConfig.builder().build(), connAware)
                                      .subscribe(client -> testContext.verify(() -> {
                                          Assertions.assertTrue(client.isConnected());
                                          Assertions.assertTrue(client.clientId().startsWith("qwe-mqtt-"));
                                          testContext.completeNow();
                                      }), testContext::failNow);
    }

    private Buffer readCert(@NonNull String certFile) throws IOException {
        final InputStream is = MqttClientVerticleTest.class.getClassLoader().getResourceAsStream(certFile);
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);
        return Buffer.buffer(targetArray);
    }

    @Test
    @Disabled
    void test(Vertx vertx, VertxTestContext testContext) {
        Checkpoint cp = testContext.checkpoint(100);
        final BasicCredential credential = BasicCredential.builder().user("1").password("1").build();
        final MqttConnectionAware connAware = MqttConnectionAware.builder()
                                                                 .host(EMQX_HOST)
                                                                 .credential(credential)
                                                                 .build();
        final MqttClientOptions options = new MqttClientOptions().setClientId("123")
                                                                 .setUsername(credential.getUser())
                                                                 .setPassword(credential.getPassword());
        new MqttSubscriber(sharedData).connect(MqttSubscriberConfig.builder().build(), connAware);
        MqttClient c1 = MqttClient.create(vertx, options);
        String topic = "xxx";
        c1.connect(1883, EMQX_HOST, s -> {
            cp.flag();
            if (s.succeeded()) {
                final MqttConnAckMessage result = s.result();
                System.out.println("Ack Conn: " + result.code() + ":" + result.isSessionPresent());
                c1.ping().exceptionHandler(Throwable::printStackTrace).subscribeCompletionHandler(ack -> {
                    System.out.println("Subscribe ACK - Msg Id:" + ack.messageId());
                    System.out.println("Subscribe ACK - QoS:" + ack.grantedQoSLevels());
                    System.out.println("---------------------------------");
                }).publishHandler(x -> {
                    System.out.println("---------------------------------");
                    System.out.println("There are new message in topic: " + x.topicName());
                    System.out.println("Content(as string) of the message: " + x.payload().toString());
                    System.out.println("QoS: " + x.qosLevel());
                    System.out.println("---------------------------------");
                }).subscribe(topic, 1, handler -> {
                    if (handler.succeeded()) {
                        System.out.println("Subscriber:" + handler.result());
                    } else {
                        testContext.failNow(handler.cause());
                    }
                });
            } else {
                testContext.failNow(s.cause());
            }
        });

        MqttClient c2 = MqttClient.create(vertx, new MqttClientOptions().setKeepAliveInterval(30)
                                                                        .setClientId("4444444")
                                                                        .setUsername(credential.getUser())
                                                                        .setPassword(credential.getPassword()));
        c2.connect(1883, "emqx.cba.nube-iot.com", s -> {
            if (s.succeeded()) {
                final MqttConnAckMessage result = s.result();
                System.out.println(result.code());
                c2.ping()
                  .pingResponseHandler(a -> System.out.println("PING RESPONSE: "))
                  .exceptionHandler(Throwable::printStackTrace)
                  .publishCompletionHandler(ok -> System.out.println("Published id OK: " + ok))
                  .publishCompletionExpirationHandler(notOk -> System.out.println("Published id NOT OK: " + notOk))
                  .publishCompletionUnknownPacketIdHandler(
                      after -> System.out.println("Published id After Expired Not OK: " + after));
                vertx.setPeriodic(2000, id -> {
                    final Buffer data = new JsonObject().put("a", 1).put("random", random()).toBuffer();
                    if (c2.isConnected()) {
                        c2.publish(topic, data, MqttQoS.AT_LEAST_ONCE, true, true, r -> {
                            if (r.succeeded()) {
                                System.out.println("Publisher:" + r.result());
                            } else {
                                testContext.failNow(r.cause());
                            }
                        });
                    } else {
                        System.out.println("DISCONNECTED");
                    }
                });
            } else {
                testContext.failNow(s.cause());
            }
        });
    }

    @SneakyThrows
    private int random() {
        return SecureRandom.getInstanceStrong().ints(10, 40).findAny().getAsInt();
    }

}
