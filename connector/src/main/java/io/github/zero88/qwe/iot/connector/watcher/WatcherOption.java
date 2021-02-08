package io.github.zero88.qwe.iot.connector.watcher;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.scheduler.model.trigger.TriggerOption;
import io.github.zero88.qwe.scheduler.model.trigger.TriggerType;
import io.vertx.core.json.JsonObject;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines watcher mechanism in realtime or polling
 *
 * @see WatcherType
 */
@Data
@Builder
@Jacksonized
public final class WatcherOption implements JsonData {

    /**
     * Enable realtime mode that run when any event is occurred in a watcher object
     */
    @Default
    private final boolean realtime = true;
    /**
     * Defines a real-time watcher is maintained in how long
     *
     * @apiNote Default is {@code -1} mean no expired
     */
    @Default
    private final int lifetimeInSeconds = -1;

    /**
     * Fallback to polling mechanism with default trigger option if {@code realtime} mechanism is not supported
     *
     * @see #triggerOption
     */
    @Default
    private final boolean fallbackPolling = true;

    /**
     * Enable polling mode that run on a schedule or periodical, such as reading a sensor every five milliseconds
     */
    @Default
    private final boolean polling = false;

    /**
     * Defines trigger option if enable trigger mode.
     *
     * @apiNote Default option is {@code periodic} with {@code interval = 5}
     */
    @Default
    private final TriggerOption triggerOption = TriggerOption.builder()
                                                             .type(TriggerType.PERIODIC)
                                                             .intervalInSeconds(5)
                                                             .build();

    public static WatcherOption parse(@NonNull JsonObject watcher) {
        final WatcherOption opt = JsonData.from(watcher, WatcherOption.class);
        if (!opt.isRealtime() && !opt.isPolling()) {
            throw new IllegalArgumentException("Must enabled one of realtime or polling mechanism");
        }
        return opt;
    }

}
