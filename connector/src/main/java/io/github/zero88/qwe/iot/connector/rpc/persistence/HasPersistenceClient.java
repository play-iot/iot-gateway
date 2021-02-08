package io.github.zero88.qwe.iot.connector.rpc.persistence;

import lombok.NonNull;

public interface HasPersistenceClient<T extends PersistenceClient> {

    @NonNull T persistenceService();

}
