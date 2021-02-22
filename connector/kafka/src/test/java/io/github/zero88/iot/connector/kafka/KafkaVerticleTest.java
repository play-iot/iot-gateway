package io.github.zero88.iot.connector.kafka;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.zero88.iot.connector.kafka.handler.KafkaErrorHandler;
import io.github.zero88.iot.connector.kafka.handler.KafkaRecord;
import io.github.zero88.iot.connector.kafka.handler.producer.KafkaProducerHandler;
import io.github.zero88.iot.connector.kafka.mock.TestErrorHandler;
import io.github.zero88.iot.connector.kafka.mock.TestProducerHandler;
import io.github.zero88.qwe.JsonHelper;
import io.github.zero88.qwe.TestHelper;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.utils.DateTimes;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class KafkaVerticleTest extends KafkaVerticleTestBase {

    @Rule
    public Timeout timeout = Timeout.seconds(TEST_TIMEOUT_SEC);

    @BeforeClass
    public static void setUp() throws IOException {
        KafkaVerticleTestBase.setUp();
        kafkaCluster();
    }

    @Test
    public void test_startup_with_no_router(TestContext context) {
        startKafkaUnit(context, new KafkaRouter());
    }

    @Test
    public void test_producer_can_send(TestContext context) {
        String topic = UUID.randomUUID().toString();
        Async async = context.async(1);
        KafkaRouter router = createProducerRouter(context, async, topic, 0);
        startKafkaUnit(context, router).getContext().getProducerService().publish(topic, 0, "test", topic);
    }

    @Test
    public void test_consumer_can_read(TestContext context) {
        String topic = UUID.randomUUID().toString();
        Async async = context.async(2);
        JsonObject expected = KafkaRecord.serialize(
            new ConsumerRecord<>(topic, 0, 0, DateTimes.nowMilli(), TimestampType.CREATE_TIME, -1, -1, -1, "test",
                                 topic)).toJson(KafkaRecord.NO_HEADERS_MAPPER);
        setupEventConsumer(async, KAFKA_PUBLISHER.getAddress(), o -> {
            EventMessage message = EventMessage.initial(EventAction.CREATE, expected);
            assertResponse(context, async, message.toJson(), (JsonObject) o);
        });
        KafkaEventMetadata kafkaConsumerEvent = KafkaEventMetadata.consumer()
                                                                  .model(KAFKA_PUBLISHER)
                                                                  .topic(topic)
                                                                  .keyClass(String.class)
                                                                  .valueClass(String.class)
                                                                  .build();
        KafkaRouter router = createProducerRouter(context, async, topic, 0).registerKafkaEvent(kafkaConsumerEvent);
        router.addConsumerErrorHandler(kafkaConsumerEvent.getTechId(), errorHandler(context, async));
        KafkaVerticle unit = startKafkaUnit(context, router);
        unit.getContext().getProducerService().publish(topic, 0, "test", topic);
    }

    protected void setupEventConsumer(Async async, String address, Consumer<Object> assertOut) {
        MessageConsumer<Object> consumer = vertx.eventBus().consumer(address);
        consumer.handler(event -> {
            System.out.println("Received message from address: " + address);
            EventMessage msg = EventMessage.tryParse(event.body());
            Assert.assertNotNull(msg.getData());
            Assert.assertNotEquals(-1, msg.getData().getValue("timestamp"));
            Assert.assertNotEquals(-1, msg.getData().getValue("checksum"));
            assertOut.accept(event.body());
            consumer.unregister(v -> TestHelper.testComplete(async));
        });
    }

    private static void assertResponse(TestContext context, Async async, JsonObject expected, JsonObject actual) {
        JsonHelper.assertJson(context, async, expected, actual, IGNORE_TIMESTAMP, IGNORE_CHECKSUM, IGNORE_EPOCH);
    }

    private KafkaErrorHandler errorHandler(TestContext context, Async async) {
        return new TestErrorHandler<>(context, async, TestHelper::testComplete);
    }

    private KafkaRouter createProducerRouter(TestContext context, Async async, String topic, Integer partition) {
        KafkaProducerHandler handler = TestProducerHandler.builder()
                                                          .context(context)
                                                          .async(async)
                                                          .countdown(TestHelper::testComplete)
                                                          .topic(topic)
                                                          .partition(partition)
                                                          .build(sharedData);
        KafkaEventMetadata metadata = KafkaEventMetadata.producer()
                                                        .handler(handler)
                                                        .topic(topic)
                                                        .keyClass(String.class)
                                                        .valueClass(String.class)
                                                        .build();
        return new KafkaRouter().registerKafkaEvent(metadata)
                                .addProducerErrorHandler(metadata.getTechId(), errorHandler(context, async));
    }

}
