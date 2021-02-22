package io.github.zero88.iot.connector.kafka;

import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.CommonClientConfigs;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.utils.Configs;

public class KafkaConfigTest {

    @Test
    public void test_default() throws JSONException {
        KafkaConfig from = IConfig.from(Configs.loadJsonConfig("kafka.json"), KafkaConfig.class);
        from.getConsumerConfig().put(CommonClientConfigs.CLIENT_ID_CONFIG, "consumer");
        from.getProducerConfig().put(CommonClientConfigs.CLIENT_ID_CONFIG, "producer");
        System.out.println(from.toJson().encode());

        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.getConsumerConfig().put(CommonClientConfigs.CLIENT_ID_CONFIG, "consumer");
        kafkaConfig.getProducerConfig().put(CommonClientConfigs.CLIENT_ID_CONFIG, "producer");

        JSONAssert.assertEquals(kafkaConfig.getConsumerConfig().toJson().encode(),
                                from.getConsumerConfig().toJson().encode(), JSONCompareMode.STRICT);
        JSONAssert.assertEquals(kafkaConfig.getProducerConfig().toJson().encode(),
                                from.getProducerConfig().toJson().encode(), JSONCompareMode.STRICT);
    }

    @Test
    public void test_can_copy() {
        KafkaConfig cfg = new KafkaConfig();
        cfg.getTopicConfig().toJson().copy();
        cfg.getSecurityConfig().toJson().copy();
        cfg.getClientConfig().toJson().copy();
        cfg.getProducerConfig().toJson().copy();
        cfg.getConsumerConfig().toJson().copy();
    }

    @Test
    public void test_from_root() {
        List<String> hostExpected = Collections.singletonList("localhost:9092");
        KafkaConfig from = IConfig.from("{\"__app__\":{\"__kafka__\":{\"__client__\":{\"bootstrap" +
                                        ".servers\":[\"localhost:9092\"]},\"__security__\":{\"security" +
                                        ".protocol\":\"PLAINTEXT\"}}},\"__deploy__\":{\"ha\":false,\"instances\":1," +
                                        "\"maxWorkerExecuteTime\":60000000000," +
                                        "\"maxWorkerExecuteTimeUnit\":\"NANOSECONDS\",\"multiThreaded\":false," +
                                        "\"worker\":false,\"workerPoolSize\":20}}", KafkaConfig.class);
        Assert.assertEquals("PLAINTEXT", from.getSecurityConfig().get("security.protocol"));
        Assert.assertEquals(hostExpected, from.getClientConfig().get("bootstrap.servers"));
        Assert.assertEquals("PLAINTEXT", from.getProducerConfig().get("security.protocol"));
        Assert.assertEquals(hostExpected, from.getConsumerConfig().get("bootstrap.servers"));
        Assert.assertEquals(hostExpected, from.getProducerConfig().get("bootstrap.servers"));
    }

}
