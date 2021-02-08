package io.github.zero88.qwe.iot.connector.command;

import java.util.Collections;

import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.iot.connector.FunctionServiceApis;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.vertx.core.http.HttpMethod;

import lombok.NonNull;

public interface CommanderApis extends Commander, FunctionServiceApis {

    @Override
    default @NonNull ActionMethodMapping eventMethodMap() {
        return ActionMethodMapping.create(Collections.singletonMap(EventAction.SEND, HttpMethod.POST));
    }

}
