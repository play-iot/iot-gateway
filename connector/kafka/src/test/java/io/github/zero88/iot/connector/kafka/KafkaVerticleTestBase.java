package io.github.zero88.iot.connector.kafka;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.kafka.clients.CommonClientConfigs;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.skyscreamer.jsonassert.Customization;
import org.slf4j.LoggerFactory;

import io.debezium.kafka.KafkaCluster;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.component.ComponentTestHelper;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventModel;
import io.github.zero88.qwe.event.EventPattern;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class KafkaVerticleTestBase {

    public static final EventModel KAFKA_PUBLISHER = EventModel.builder()
                                                               .address("kafka.broadcaster")
                                                               .pattern(EventPattern.PUBLISH_SUBSCRIBE)
                                                               .event(EventAction.CREATE)
                                                               .build();
    static final Customization IGNORE_TIMESTAMP = new Customization("data.timestamp", (o1, o2) -> true);
    static final Customization IGNORE_CHECKSUM = new Customization("data.checksum", (o1, o2) -> true);
    static final Customization IGNORE_HEADERS = new Customization("data.headers", (o1, o2) -> true);
    static final Customization IGNORE_EPOCH = new Customization("data.leaderEpoch", (o1, o2) -> true);
    static final int TEST_TIMEOUT_SEC = 8;
    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();
    protected static KafkaCluster kafkaCluster;
    protected static int kafkaPort;
    private static File dataDir;
    protected Vertx vertx;
    protected KafkaConfig kafkaConfig;
    protected SharedDataLocalProxy sharedData;

    @BeforeClass
    public static void setUp() throws IOException {
        TestHelper.setup();
        ((Logger) LoggerFactory.getLogger("org.apache.zookeeper")).setLevel(Level.WARN);
        ((Logger) LoggerFactory.getLogger("kafka")).setLevel(Level.INFO);
        ((Logger) LoggerFactory.getLogger("org.apache.kafka")).setLevel(Level.INFO);
        ((Logger) LoggerFactory.getLogger("org.apache.kafka.clients")).setLevel(Level.INFO);
    }

    @AfterClass
    public static void tearDown() {
        if (Objects.nonNull(kafkaCluster)) {
            kafkaCluster.shutdown();
            kafkaCluster = null;
        }
    }

    static void kafkaCluster() throws IOException {
        kafkaCluster(tempFolder);
    }

    static void kafkaCluster(TemporaryFolder tempFolder) throws IOException {
        if (kafkaCluster != null) {
            throw new IllegalStateException();
        }
        dataDir = tempFolder.newFolder("kafka");
        kafkaPort = TestHelper.getRandomPort();
        kafkaCluster = new KafkaCluster().usingDirectory(dataDir)
                                         .withPorts(TestHelper.getRandomPort(), kafkaPort)
                                         .deleteDataPriorToStartup(true)
                                         .deleteDataUponShutdown(true)
                                         .addBrokers(1)
                                         .startup();
    }

    static KafkaConfig createKafkaConfig() {
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.getSecurityConfig().put("security.protocol", "PLAINTEXT");
        kafkaConfig.getClientConfig().put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:" + kafkaPort);
        return kafkaConfig;
    }

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
        kafkaConfig = createKafkaConfig();
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    KafkaVerticle startKafkaUnit(TestContext context, KafkaRouter router) {
        final KafkaVerticle verticle = ComponentTestHelper.deploy(vertx, context, kafkaConfig.toJson(),
                                                                new KafkaProvider(router), dataDir.toPath());
        sharedData = verticle.sharedData();
        return verticle;
    }

}
