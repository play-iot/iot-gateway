package io.github.zero88.iot.connector.kafka;

import io.github.zero88.qwe.component.ComponentContext;
import io.github.zero88.qwe.component.ComponentVerticle;
import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.vertx.core.Promise;

import lombok.NonNull;

/**
 * Handle open/close Kafka client
 */
public final class KafkaVerticle extends ComponentVerticle<KafkaConfig, KafkaContext> {

    private final KafkaRouter router;

    KafkaVerticle(SharedDataLocalProxy sharedData, KafkaRouter router) {
        super(sharedData);
        this.router = router;
    }

    @Override
    public Class<KafkaConfig> configClass() { return KafkaConfig.class; }

    @Override
    public String configFile() { return "kafka.json"; }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop();
        this.getContext().stop().subscribe(stopPromise::complete);
    }

    public KafkaContext onSuccess(@NonNull ComponentContext context) {
        this.logger.info("Setup Kafka context...");
        return new KafkaContext(context).setup(this.sharedData(), this.config, this.router);
    }

}
