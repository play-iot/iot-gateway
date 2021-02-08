package io.github.zero88.qwe.iot.connector.coordinator;

import java.util.Arrays;
import java.util.Collection;

import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventContractor.Param;
import io.github.zero88.qwe.event.EventPattern;
import io.github.zero88.qwe.event.Waybill;
import io.github.zero88.qwe.iot.connector.ConnectorService;
import io.github.zero88.qwe.iot.connector.FunctionService;
import io.github.zero88.qwe.iot.connector.Subject;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

/**
 * Represents for an {@code coordinator service} that supervises a particular {@code subject} then notifying it to the
 * registered {@code subscribers}.
 * <p>
 * The end-to-end process is named as a {@code coordinator channel}
 *
 * @param <S> Type of subject
 * @see Subject
 * @see ConnectorService
 */
public interface Coordinator<S extends Subject> extends FunctionService {

    @Override
    default String domain() {
        return "coordinator";
    }

    /**
     * Register a {@code coordinator channel}
     *
     * @param requestData request data
     * @return coordinator channel
     * @see #parseCoordinatorInput(RequestData)
     * @see CoordinatorChannel
     */
    @EventContractor(action = "CREATE_OR_UPDATE", returnType = Single.class)
    Single<CoordinatorChannel> register(@NonNull RequestData requestData);

    /**
     * Unregister a {@code coordinator channel}
     *
     * @param requestData request data
     * @return coordinator output
     * @see CoordinatorChannel
     */
    @EventContractor(action = "REMOVE", returnType = Single.class)
    Single<CoordinatorChannel> unregister(@NonNull RequestData requestData);

    /**
     * Query a {@code coordinator channel} by {@code subject}
     *
     * @param requestData request data
     * @return coordinator output
     * @see CoordinatorChannel
     */
    @EventContractor(action = "GET_ONE", returnType = Single.class)
    Single<CoordinatorChannel> get(@NonNull RequestData requestData);

    /**
     * Defines a handler that listens an event of {@code subject} then notifies to list of {@code subscribers}
     *
     * @param data  subject event data
     * @param error error message if any error
     * @return true as ack
     */
    @EventContractor(action = "MONITOR", returnType = boolean.class)
    boolean superviseThenNotify(@Param("data") JsonObject data, @Param("error") ErrorMessage error);

    /**
     * Parse a coordinator input from request to prepare {@code coordinator channel}
     *
     * @param requestData request data
     * @return coordinator input
     */
    @NonNull CoordinatorInput<S> parseCoordinatorInput(@NonNull RequestData requestData);

    @Override
    default @NonNull Collection<EventAction> getAvailableEvents() {
        return Arrays.asList(EventAction.CREATE_OR_UPDATE, EventAction.REMOVE, EventAction.GET_ONE, EventAction.PATCH,
                             EventAction.MONITOR);
    }

    /**
     * Declares a coordinator address to register a callback address when a {@code subject} notifies an event or change
     *
     * @return waybill
     * @see #superviseThenNotify(JsonObject, ErrorMessage)
     * @see Waybill
     */
    default Waybill coordinatorInfo() {
        return Waybill.builder()
                      .address(this.getClass().getName())
                      .action(EventAction.MONITOR)
                      .pattern(EventPattern.PUBLISH_SUBSCRIBE)
                      .build();
    }

    /**
     * Defines an {@code subject} address that want to supervise is where a {@code coordinator} can ask
     *
     * @param payload request data that send to {@code subject} address
     * @return waybill
     * @see Waybill
     */
    Waybill subjectInfo(JsonObject payload);

}
