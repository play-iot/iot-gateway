package io.github.zero88.qwe.iot.demo.kafka;

import java.util.Collections;

import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.http.server.rest.api.AbstractRestEventApi;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.vertx.core.http.HttpMethod;

public final class EnableKafkaProducerApi extends AbstractRestEventApi {

    @Override
    public EnableKafkaProducerApi initRouter() {
        addRouter(KafkaConsumerDashboard.KAFKA_ENABLED, "/kafka/enable");
        return this;
    }

    @Override
    protected ActionMethodMapping initHttpEventMap() {
        return ActionMethodMapping.create(Collections.singletonMap(EventAction.UPDATE, HttpMethod.POST));
    }

}
