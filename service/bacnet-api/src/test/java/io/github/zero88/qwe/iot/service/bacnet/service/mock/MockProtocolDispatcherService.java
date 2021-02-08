package io.github.zero88.qwe.iot.service.bacnet.service.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventListener;
import io.github.zero88.qwe.event.Status;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.github.zero88.qwe.micro.http.EventHttpService;
import io.github.zero88.qwe.micro.http.EventMethodDefinition;
import io.reactivex.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

public class MockProtocolDispatcherService implements EventListener, EventHttpService {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String api() {
        return "";
    }

    @Override
    public Set<EventMethodDefinition> definitions() {
        final ActionMethodMapping mapping = ActionMethodMapping.create(
            Collections.singletonMap(EventAction.CREATE_OR_UPDATE, HttpMethod.valueOf("OTHER")));
        return Collections.singleton(EventMethodDefinition.create("/dispatcher", "dispatcher_id", mapping));
    }

    @Override
    public @NonNull Collection<EventAction> getAvailableEvents() {
        return Arrays.asList(EventAction.GET_ONE, EventAction.CREATE_OR_UPDATE);
    }

    @EventContractor(action = "CREATE_OR_UPDATE", returnType = Single.class)
    public Single<JsonObject> createOrUpdate(RequestData reqData) {
        return Single.just(new JsonObject().put("action", EventAction.CREATE)
                                           .put("status", Status.SUCCESS)
                                           .put("resource", "resource"));
    }

}
