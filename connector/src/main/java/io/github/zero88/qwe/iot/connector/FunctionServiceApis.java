package io.github.zero88.qwe.iot.connector;

import io.github.zero88.utils.Urls;

public interface FunctionServiceApis extends FunctionService, ConnectorServiceApis {

    default String fullServicePath() {
        return Urls.combinePath(domain(), protocol().type().toLowerCase(), servicePath(), function());
    }

    @Override
    default String paramPath() {
        return null;
    }

}
