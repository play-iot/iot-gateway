package io.github.zero88.qwe.iot.connector.coordinator;

import java.util.List;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.Subject;
import io.github.zero88.qwe.iot.connector.subscriber.Subscriber;
import io.github.zero88.qwe.iot.connector.watcher.WatcherOption;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * Coordinator input to prepare a {@code coordinator channel}
 *
 * @param <S> Type of Subject
 * @see Subject
 * @see Subscriber
 * @see WatcherOption
 */
@Data
@Builder
@Jacksonized
@FieldNameConstants
public final class CoordinatorInput<S extends Subject> implements JsonData {

    @NonNull
    private final S subject;
    @Default
    @NonNull
    private final WatcherOption watcherOption = WatcherOption.builder().build();
    @NonNull
    @Singular
    private final List<Subscriber> subscribers;

    public CoordinatorInput<S> validate() {
        if (getSubscribers().isEmpty()) {
            throw new IllegalArgumentException("Must provides at least subscribers");
        }
        return this;
    }

}
