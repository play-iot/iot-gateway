package io.github.zero88.qwe.iot.connector.coordinator;

import java.util.Collection;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventListener;
import io.github.zero88.qwe.iot.data.IoTEntity;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.reactivex.Single;

import lombok.NonNull;

/**
 * Represents for a {@code RpcClient service} that listens an {@code external event} from outside services then
 * dispatching event to a corresponding inner service handler
 *
 * @param <P> Type of entity object
 * @see EventListener
 */
public interface OutboundCoordinator<P extends IoTEntity> extends EventListener {

    @Override
    default @NonNull Collection<EventAction> getAvailableEvents() {
        return ActionMethodMapping.DML_MAP.get().keySet();
    }

    /**
     * Defines itself address in eventbus network
     *
     * @return Eventbus address
     */
    default String address() {
        return this.getClass().getName();
    }

    /**
     * Defines whether listening global event in {@code declared entity} regardless if entity protocol isn't matched
     * with declared protocol
     *
     * @return {@code true} if global
     */
    default boolean isGlobal() {
        return false;
    }

    /**
     * Defines listener for updating existing resource by primary key
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#CREATE
     */
    @EventContractor(action = "CREATE", returnType = Single.class)
    @NonNull Single<P> create(@NonNull RequestData requestData);

    /**
     * Defines listener for updating existing resource by primary key
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#UPDATE
     */
    @EventContractor(action = "UPDATE", returnType = Single.class)
    @NonNull Single<P> update(@NonNull RequestData requestData);

    /**
     * Defines listener for patching existing resource by primary key
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#PATCH
     */
    @EventContractor(action = "PATCH", returnType = Single.class)
    @NonNull Single<P> patch(@NonNull RequestData requestData);

    /**
     * Defines listener for deleting existing resource by primary key
     *
     * @param requestData Request data
     * @return json object that includes status message
     * @see EventAction#REMOVE
     */
    @EventContractor(action = "REMOVE", returnType = Single.class)
    @NonNull Single<P> delete(@NonNull RequestData requestData);

}
