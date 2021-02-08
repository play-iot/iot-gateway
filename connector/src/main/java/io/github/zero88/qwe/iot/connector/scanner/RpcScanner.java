package io.github.zero88.qwe.iot.connector.scanner;

import io.github.zero88.qwe.iot.connector.ConnectorService;
import io.github.zero88.qwe.iot.data.IoTEntity;

import lombok.NonNull;

/**
 * Represents for a {@code RpcClient service} that is able to scan from a specific external source to extract an
 * appropriate {@code protocol} data object then using them to initialize itself service in startup.
 *
 * @param <P> Type of {@code IoTEntity}
 * @see ScannerSource
 * @since 1.0.0
 */
public interface RpcScanner<P extends IoTEntity> extends ConnectorService {

    @NonNull ScannerSource source();

    /**
     * Declares context that represents for the protocol entity
     *
     * @return class of protocol entity
     * @see IoTEntity
     */
    @NonNull Class<P> context();

}
