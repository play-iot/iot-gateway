package io.github.zero88.qwe.iot.connector.coordinator;

import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventContractor.Param;
import io.github.zero88.qwe.iot.connector.Subject;
import io.github.zero88.qwe.iot.connector.rpc.persistence.HasPersistenceClient;
import io.github.zero88.qwe.iot.connector.rpc.persistence.PersistenceClient;
import io.github.zero88.qwe.iot.connector.rpc.scheduler.HasSchedulerClient;
import io.github.zero88.qwe.iot.connector.watcher.WatcherOption;
import io.github.zero88.qwe.iot.connector.watcher.WatcherType;
import io.github.zero88.qwe.scheduler.model.job.EventbusJobModel;
import io.github.zero88.qwe.scheduler.model.job.QWEJobModel;
import io.github.zero88.qwe.scheduler.model.trigger.QWETriggerModel;
import io.github.zero88.qwe.scheduler.service.SchedulerRegisterArgs;
import io.github.zero88.qwe.scheduler.service.SchedulerRegisterResp;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import lombok.NonNull;

/**
 * Represents for an {@code coordinator service} that watches a particular {@code internal subject} then notifying this
 * events to external {@code subscribers}.
 *
 * @param <S> Type of subject
 * @see Subject
 * @see Coordinator
 * @see HasSchedulerClient
 * @see HasPersistenceClient
 */
public interface InboundCoordinator<S extends Subject, T extends PersistenceClient>
    extends Coordinator<S>, HasSchedulerClient, HasPersistenceClient<T> {

    @EventContractor(action = "CREATE_OR_UPDATE", returnType = Single.class)
    default Single<CoordinatorChannel> register(@NonNull RequestData requestData) {
        return this.validateInCreation(parseCoordinatorInput(requestData)).flatMap(this::addWatcher);
    }

    @EventContractor(action = "REMOVE", returnType = Single.class)
    default Single<CoordinatorChannel> unregister(@NonNull RequestData requestData) {
        return Single.just(parseCoordinatorInput(requestData))
                     .flatMap(input -> validateInQuery(input).flatMap(channel -> this.removeWatcher(input, channel)));
    }

    @EventContractor(action = "GET_ONE", returnType = Single.class)
    default Single<CoordinatorChannel> get(@NonNull RequestData requestData) {
        return Single.just(parseCoordinatorInput(requestData))
                     .flatMap(this::validateInQuery)
                     .flatMap(this::getCurrentWatcherStatus);
    }

    @EventContractor(action = "MONITOR", returnType = boolean.class)
    boolean superviseThenNotify(@Param("data") JsonObject data, @Param("error") ErrorMessage error);

    default Single<CoordinatorInput<S>> validateInCreation(@NonNull CoordinatorInput<S> input) {
        return Single.just(input.validate());
    }

    Single<CoordinatorChannel> validateInQuery(@NonNull CoordinatorInput<S> input);

    default Single<CoordinatorChannel> addWatcher(CoordinatorInput<S> input) {
        return Single.just(input.getWatcherOption())
                     .filter(WatcherOption::isRealtime)
                     .flatMapSingleElement(ignore -> addRealtimeWatcher(input))
                     .switchIfEmpty(addPollingWatcher(input));
    }

    default Single<CoordinatorChannel> getCurrentWatcherStatus(@NonNull CoordinatorChannel channel) {
        return Single.just(channel.getWatcherType())
                     .filter(t -> t == WatcherType.POLLING)
                     .flatMapSingleElement(ignore -> getCurrentPollingWatcher(channel))
                     .switchIfEmpty(getCurrentRealtimeWatcher(channel));
    }

    default Single<CoordinatorChannel> removeWatcher(@NonNull CoordinatorInput<S> coordinatorInput,
                                                     @NonNull CoordinatorChannel coordinatorChannel) {
        if (coordinatorChannel.getWatcherType() == WatcherType.REALTIME) {
            return removeRealtimeWatcher(coordinatorInput, coordinatorChannel);
        }
        if (coordinatorChannel.getWatcherType() == WatcherType.POLLING) {
            return removePollingWatcher(coordinatorInput, coordinatorChannel);
        }
        throw new IllegalArgumentException("Unknown watcher type " + coordinatorChannel.getWatcherType());
    }

    Single<CoordinatorChannel> addRealtimeWatcher(@NonNull CoordinatorInput<S> coordinatorInput);

    Single<CoordinatorChannel> getCurrentRealtimeWatcher(@NonNull CoordinatorChannel channel);

    Single<CoordinatorChannel> removeRealtimeWatcher(@NonNull CoordinatorInput<S> coordinatorInput,
                                                     @NonNull CoordinatorChannel coordinatorChannel);

    Single<CoordinatorChannel> addPollingWatcher(@NonNull CoordinatorInput<S> coordinatorInput);

    default Single<CoordinatorChannel> getCurrentPollingWatcher(@NonNull CoordinatorChannel channel) {
        final SchedulerRegisterArgs args = SchedulerRegisterArgs.builder()
                                                                .jobKey(channel.getWatcherKey())
                                                                .triggerKey(channel.getWatcherKey())
                                                                .build();
        return schedulerService().execute(EventAction.GET_ONE, args,
                                          protocol().type() + ":" + function() + ":" + domain() + " polling watcher")
                                 .map(resp -> channel.watcherOutput(resp.toJson()));
    }

    default Single<CoordinatorChannel> removePollingWatcher(@NonNull CoordinatorInput<S> coordinatorInput,
                                                            @NonNull CoordinatorChannel coordinatorChannel) {
        return removeScheduler(coordinatorChannel.key()).map(resp -> coordinatorChannel.watcherOutput(resp.toJson()));
    }

    default Single<SchedulerRegisterResp> addScheduler(@NonNull WatcherOption option, @NonNull String jobName,
                                                       @NonNull String triggerName,
                                                       @NonNull JsonObject processPayload) {
        final QWEJobModel job = EventbusJobModel.builder()
                                                .group(protocol().type())
                                                .name(jobName)
                                                .process(subjectInfo(processPayload))
                                                .callback(coordinatorInfo())
                                                .build();
        final QWETriggerModel trigger = QWETriggerModel.from(protocol().type(), jobName, option.getTriggerOption());
        final SchedulerRegisterArgs args = SchedulerRegisterArgs.builder().job(job).trigger(trigger).build();
        return schedulerService().execute(EventAction.CREATE, args,
                                          protocol().type() + ":" + function() + ":" + domain() + " polling watcher");
    }

    default Single<SchedulerRegisterResp> removeScheduler(@NonNull String jobName) {
        final String jobKey = SchedulerRegisterArgs.createKey(protocol().type(), jobName);
        return schedulerService().execute(EventAction.REMOVE, SchedulerRegisterArgs.builder().jobKey(jobKey).build(),
                                          protocol().type() + ":" + function() + ":" + domain() + " polling watcher");
    }

}
