package io.github.zero88.qwe.iot.connector.discovery;

import io.github.zero88.qwe.iot.connector.ConnectorServiceApis;
import io.github.zero88.qwe.iot.data.IoTEntities;
import io.github.zero88.qwe.iot.data.IoTEntity;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.github.zero88.qwe.micro.http.EventHttpService;

import lombok.NonNull;

/**
 * Represents for {@code Discovery APIs} that expose as public endpoints
 *
 * @param <P> Type of IoT entity
 * @see ExplorerService
 * @see EventHttpService
 */
public interface ExplorerServiceApis<K, P extends IoTEntity<K>, X extends IoTEntities<K, P>>
    extends ExplorerService<K, P, X>, ConnectorServiceApis {

    /**
     * Event action and HTTP method mapping
     *
     * @return event method map
     * @see ActionMethodMapping
     */
    default @NonNull ActionMethodMapping eventMethodMap() {
        return ActionMethodMapping.DQL_MAP;
    }

}
