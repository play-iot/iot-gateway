package io.github.zero88.qwe.iot.connector.coordinator;

import io.github.zero88.qwe.iot.connector.FunctionServiceApis;
import io.github.zero88.qwe.iot.connector.Subject;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;

import lombok.NonNull;

public interface CoordinatorApis<S extends Subject> extends Coordinator<S>, FunctionServiceApis {

    @Override
    default @NonNull ActionMethodMapping eventMethodMap() {
        return ActionMethodMapping.by(ActionMethodMapping.CRD_MAP, getAvailableEvents());
    }

}
