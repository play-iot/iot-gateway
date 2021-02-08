package io.github.zero88.qwe.iot.connector;

import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.event.EventListener;
import io.github.zero88.qwe.protocol.HasProtocol;

/**
 * Represents a connector protocol service
 */
public interface ConnectorService extends EventListener, HasProtocol, HasSharedData {

    /**
     * Defines service domain name that will be used to distinguish to other domain services.
     * <p>
     * One {@code domain} service can group multiple {@code function} services. Check {@link FunctionService}.
     *
     * @return domain name
     * @apiNote It is used to generated HTTP path and Event address then it must be unique
     */
    String domain();

}
