package io.github.zero88.qwe.iot.connector.bacnet.internal.ack;

import java.util.function.Function;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;

public interface AckServiceHandler<A extends AcknowledgementService> extends Function<A, JsonObject> {

}
