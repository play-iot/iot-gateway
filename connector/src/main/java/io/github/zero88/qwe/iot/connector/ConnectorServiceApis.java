package io.github.zero88.qwe.iot.connector;

import java.util.Collections;
import java.util.Set;

import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.github.zero88.qwe.micro.http.EventHttpService;
import io.github.zero88.qwe.micro.http.EventMethodDefinition;
import io.github.zero88.utils.Urls;

import lombok.NonNull;

public interface ConnectorServiceApis extends ConnectorService, EventHttpService {

    @Override
    default String api() {
        return String.join(".", domain(), protocol().type().toLowerCase(), getClass().getSimpleName());
    }

    @Override
    default Set<EventMethodDefinition> definitions() {
        return Collections.singleton(EventMethodDefinition.create(fullServicePath(), paramPath(), eventMethodMap()));
    }

    /**
     * Full HTTP service path
     *
     * @return full HTTP service path
     */
    default String fullServicePath() {
        return Urls.combinePath(domain(), protocol().type().toLowerCase(), servicePath());
    }

    /**
     * Service discovery HTTP path for a specific protocol resource
     *
     * @return path
     */
    @NonNull String servicePath();

    /**
     * Parameter path for manipulating a specific protocol resource
     *
     * @return param path
     */
    String paramPath();

    /**
     * Event action and HTTP method mapping
     *
     * @return event method map
     * @see ActionMethodMapping
     * @see ConnectorService#getAvailableEvents()
     */
    @NonNull ActionMethodMapping eventMethodMap();

}
