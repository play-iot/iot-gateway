package io.github.zero88.qwe.iot.connector;

import lombok.NonNull;

/**
 * Connector function service that interacts with only one particular {@code protocol object} per one time
 *
 * @see ConnectorService
 */
public interface FunctionService extends ConnectorService {

    @NonNull String function();

}
