package io.github.zero88.qwe.iot.service.bacnet.service;

import io.github.zero88.qwe.iot.connector.FunctionServiceApis;
import io.github.zero88.utils.Urls;

import lombok.NonNull;

public interface BACnetFunctionApis extends FunctionServiceApis, BACnetApis {

    @Override
    default @NonNull String servicePath() {
        return Urls.combinePath(BACnetApis.super.servicePath(), BACnetApis.super.paramPath());
    }

    @Override
    default String paramPath() {
        return FunctionServiceApis.super.paramPath();
    }

}
