package io.github.zero88.iot.connector.kafka;

import org.junit.runner.RunWith;

import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class KafkaWebsocketTest { /*extends HttpServerTestBase {

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();
    private KafkaConfig kafkaConfig;
    private MockKafkaConsumer consumer;
    private MockKafkaProducer producer;

    @BeforeClass
    public static void beforeSuite() throws IOException {
        TestHelper.setup();
        KafkaUnitTestBase.setUp();
        KafkaUnitTestBase.kafkaCluster(tempFolder);
    }

    @AfterClass
    public static void tearDown() {
        KafkaUnitTestBase.tearDown();
    }

    @Before
    public void before(TestContext context) throws IOException {
        super.before(context);
        this.enableWebsocket();
        this.kafkaConfig = KafkaUnitTestBase.createKafkaConfig();
    }

    @After
    public void after(TestContext context) {
        if (Objects.nonNull(consumer)) {
            consumer.stop();
        }
        if (Objects.nonNull(producer)) {
            producer.stop();
        }
        super.after(context);
    }

    @Test
    public void test_client_consumer(TestContext context) {
        WebsocketServerEventMetadata metadata = MockWebsocketEvent.ONLY_PUBLISHER;
        startServer(context, new HttpServerRouter().registerEventBusSocket(metadata));
        startKafkaClient(metadata);
        Async async = context.async(1);
        assertJsonData(async, metadata.getPublisher().getAddress(),
                       JsonHelper.asserter(context, async, supply().get().toJson()));
    }

    @Test
    public void test_web_consumer(TestContext context) throws InterruptedException {
        WebsocketServerEventMetadata metadata = MockWebsocketEvent.ONLY_PUBLISHER;
        JsonObject expected = createWebsocketMsg(metadata.getPublisher().getAddress(), supply().get(),
                                                 BridgeEventType.RECEIVE);
        startServer(context, new HttpServerRouter().registerEventBusSocket(metadata));
        startKafkaClient(metadata);
        Async async = context.async(1);
        WebSocket ws = setupSockJsClient(context, async, Urls.combinePath("ws", metadata.getPath()),
                                         clientRegister(metadata.getPublisher().getAddress()), context::fail);
        ws.handler(buffer -> JsonHelper.assertJson(context, async, expected, buffer));
    }

    private void startKafkaClient(WebsocketServerEventMetadata metadata) {
        consumer = new MockKafkaConsumer(vertx.getDelegate(), kafkaConfig.getConsumerConfig(), "qwe",
                                         metadata::getPublisher);
        producer = new MockKafkaProducer(vertx.getDelegate(), kafkaConfig.getProducerConfig(), "qwe", supply());
        consumer.start();
        producer.start();
    }

    private Supplier<EventMessage> supply() {
        return () -> EventMessage.success(EventAction.RETURN, new JsonObject().put("hello", "kafka"));
    }*/

}
