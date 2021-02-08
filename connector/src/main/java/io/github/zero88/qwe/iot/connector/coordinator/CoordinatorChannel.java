package io.github.zero88.qwe.iot.connector.coordinator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.connector.Subject;
import io.github.zero88.qwe.iot.connector.subscriber.Subscriber;
import io.github.zero88.qwe.iot.connector.watcher.WatcherOption;
import io.github.zero88.qwe.iot.connector.watcher.WatcherType;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents for a coordinator channel
 */
@Data
@Builder
@Jacksonized
@FieldNameConstants
public final class CoordinatorChannel implements JsonData {

    private final String key;
    private final JsonObject subject;
    private final WatcherType watcherType;
    private final WatcherOption watcherOption;
    private final String watcherKey;
    @Singular
    private final List<JsonObject> subscribers;
    @Setter
    @Accessors(fluent = true)
    @JsonProperty(Fields.watcherOutput)
    private JsonObject watcherOutput;

    public static @NonNull CoordinatorChannel from(@NonNull CoordinatorInput<? extends Subject> input,
                                                   @NonNull WatcherType watcherType, @NonNull String watcherKey,
                                                   JsonObject watcherOutput) {
        return CoordinatorChannel.builder()
                                 .key(input.getSubject().key())
                                 .watcherType(watcherType)
                                 .watcherKey(watcherKey)
                                 .watcherOutput(watcherOutput)
                                 .watcherOption(input.getWatcherOption())
                                 .subject(input.getSubject().toDetail())
                                 .subscribers(input.getSubscribers()
                                                   .stream()
                                                   .map(Subscriber::toJson)
                                                   .collect(Collectors.toList()))
                                 .build();
    }

    @JsonProperty(Fields.watcherType)
    public String watcherType() {
        return watcherType.type();
    }

    private String getKey() {
        return key;
    }

    @JsonProperty(Fields.key)
    public String key() {
        return Optional.ofNullable(getKey())
                       .orElseGet(() -> Optional.ofNullable(subject).map(s -> s.getString("key")).orElse(null));
    }

    public JsonObject persist() {
        return toJson(Collections.singleton(Fields.watcherOutput));
    }

}
