package io.github.zero88.qwe.iot.connector.command;

import java.util.Collection;
import java.util.Collections;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.iot.connector.ConnectorService;
import io.github.zero88.qwe.iot.connector.FunctionService;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

/**
 * Represents for ad-hoc command that send a specific request to a particular {@code protocol device}
 *
 * @see ConnectorService
 */
public interface Commander extends FunctionService {

    @Override
    default String domain() {
        return "command";
    }

    @EventContractor(action = "SEND", returnType = Single.class)
    Single<JsonObject> send(@NonNull RequestData requestData);

    @Override
    default @NonNull Collection<EventAction> getAvailableEvents() {
        return Collections.singletonList(EventAction.SEND);
    }

}
