package io.github.zero88.qwe.iot.service.bacnet.service.coordinator;

import java.util.Objects;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventContractor.Param;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.event.Waybill;
import io.github.zero88.qwe.exceptions.CarlException;
import io.github.zero88.qwe.exceptions.NotFoundException;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.SubscribeCOVRequestFactory;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.SubscribeCOVRequestFactory.SubscribeCOVOptions;
import io.github.zero88.qwe.iot.service.bacnet.service.scheduler.BACnetSchedulerClient;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.service.bacnet.service.AbstractBACnetService;
import io.github.zero88.qwe.iot.service.bacnet.service.BACnetFunctionApis;
import io.github.zero88.qwe.iot.service.bacnet.service.command.ReadPointValueCommander;
import io.github.zero88.qwe.iot.service.bacnet.websocket.WebSocketCOVSubscriber;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorApis;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorChannel;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorInput;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorInput.Fields;
import io.github.zero88.qwe.iot.connector.coordinator.InboundCoordinator;
import io.github.zero88.qwe.iot.connector.subscriber.Subscriber;
import io.github.zero88.qwe.iot.connector.watcher.WatcherOption;
import io.github.zero88.qwe.iot.connector.watcher.WatcherType;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class BACnetCOVCoordinator extends AbstractBACnetService
    implements InboundCoordinator<DiscoveryArguments, CovCoordinatorPersistence>, CoordinatorApis<DiscoveryArguments>,
               BACnetFunctionApis {

    private final CovCoordinatorPersistence persistenceService;
    private final BACnetSchedulerClient schedulerService;

    BACnetCOVCoordinator(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
        this.persistenceService = new CovCoordinatorPersistence(sharedData());
        this.schedulerService = new BACnetSchedulerClient(sharedData());
    }

    @Override
    public @NonNull String function() {
        return "cov";
    }

    @Override
    public DiscoveryLevel level() {
        return DiscoveryLevel.OBJECT;
    }

    @Override
    @EventContractor(action = "CREATE_OR_UPDATE", returnType = Single.class)
    public Single<CoordinatorChannel> register(@NonNull RequestData requestData) {
        return InboundCoordinator.super.register(requestData).flatMap(persistenceService()::createOrUpdate);
    }

    @Override
    @EventContractor(action = "REMOVE", returnType = Single.class)
    public Single<CoordinatorChannel> unregister(@NonNull RequestData requestData) {
        return InboundCoordinator.super.unregister(requestData).flatMap(persistenceService()::remove);
    }

    @Override
    @EventContractor(action = "GET_ONE", returnType = Single.class)
    public Single<CoordinatorChannel> get(@NonNull RequestData requestData) {
        return InboundCoordinator.super.get(requestData);
    }

    @Override
    @EventContractor(action = "MONITOR", returnType = boolean.class)
    public boolean superviseThenNotify(@Param("data") JsonObject data, @Param("error") ErrorMessage error) {
        final EventbusClient eb = EventbusClient.create(sharedData());
        final EventMessage msg = Objects.nonNull(error)
                                 ? EventMessage.error(EventAction.MONITOR, error)
                                 : EventMessage.success(EventAction.MONITOR, data);
        eb.publish(WebSocketCOVSubscriber.builder().build().getPublishAddress(), msg);
        return true;
    }

    @Override
    public Single<CoordinatorChannel> validateInQuery(@NonNull CoordinatorInput<DiscoveryArguments> input) {
        //TODO try to find scheduler or realtime if it has schedule but not in file
        final String key = input.getSubject().key();
        return persistenceService().get(key)
                                   .switchIfEmpty(Single.error(new NotFoundException("Not found subject " + key)));
    }

    @Override
    public Single<CoordinatorChannel> addRealtimeWatcher(@NonNull CoordinatorInput<DiscoveryArguments> input) {
        final DiscoveryArguments args = input.getSubject();
        final BACnetDevice device = getLocalDeviceFromCache(args);
        final int lifetime = input.getWatcherOption().getLifetimeInSeconds();
        final int processId = device.localDevice().getNextProcessId();
        final SubscribeCOVOptions opt = SubscribeCOVOptions.builder()
                                                           .subscribe(true)
                                                           .processId(processId)
                                                           .lifetime(Math.max(lifetime, 0))
                                                           .build();
        return device.send(EventAction.CREATE, args,
                           RequestData.builder().filter(opt.toJson()).body(coordinatorInfo().toJson()).build(),
                           new SubscribeCOVRequestFactory()).flatMap(msg -> fallbackIfFailure(input, msg, processId));
    }

    @Override
    public Single<CoordinatorChannel> getCurrentRealtimeWatcher(@NonNull CoordinatorChannel channel) {
        return Single.just(channel);
    }

    @Override
    public Single<CoordinatorChannel> removeRealtimeWatcher(@NonNull CoordinatorInput<DiscoveryArguments> input,
                                                            @NonNull CoordinatorChannel channel) {
        final SubscribeCOVOptions opt = SubscribeCOVOptions.builder().subscribe(false).build();
        final BACnetDevice device = getLocalDeviceFromCache(input.getSubject());
        return device.send(EventAction.REMOVE, input.getSubject(), RequestData.builder().filter(opt.toJson()).build(),
                           new SubscribeCOVRequestFactory()).map(c -> channel.watcherOutput(c.toJson()));
    }

    @Override
    public Single<CoordinatorChannel> addPollingWatcher(@NonNull CoordinatorInput<DiscoveryArguments> input) {
        final DiscoveryArguments args = input.getSubject();
        final WatcherOption option = input.getWatcherOption();
        final BACnetDevice device = getLocalDeviceFromCache(args);
        final String key = args.key();
        final JsonObject filter = args.options().toJson().put(ReadPointValueCommander.AS_COV, true);
        final RequestData requestData = RequestData.builder().body(args.params().toJson()).filter(filter).build();
        return device.discoverRemoteObject(args)
                     .flatMap(pvm -> addScheduler(option, key, key, requestData.toJson()))
                     .map(res -> CoordinatorChannel.from(input, WatcherType.POLLING, res.getJobKey(), res.toJson()));
    }

    @Override
    public Single<CoordinatorChannel> removePollingWatcher(@NonNull CoordinatorInput<DiscoveryArguments> input,
                                                           @NonNull CoordinatorChannel channel) {
        return removeScheduler(channel.key()).map(resp -> channel.watcherOutput(resp.toJson()));
    }

    @Override
    public @NonNull CoordinatorInput<DiscoveryArguments> parseCoordinatorInput(@NonNull RequestData requestData) {
        final DiscoveryArguments args = createDiscoveryArgs(requestData, level());
        final JsonObject body = requestData.body();
        final WatcherOption option = WatcherOption.parse(body.getJsonObject(Fields.watcherOption, new JsonObject()));
        final Subscriber subscriber = WebSocketCOVSubscriber.builder().build();
        return CoordinatorInput.<DiscoveryArguments>builder().subject(args)
                                                             .watcherOption(option)
                                                             .subscriber(subscriber)
                                                             .build();
    }

    @Override
    public Waybill subjectInfo(JsonObject payload) {
        return Waybill.builder().address(ReadPointValueCommander.class.getName())
                      .action(EventAction.SEND)
                      .payload(payload)
                      .build();
    }

    private Single<CoordinatorChannel> fallbackIfFailure(CoordinatorInput<DiscoveryArguments> input, EventMessage msg,
                                                         int processId) {
        if (!msg.isError()) {
            return Single.just(
                CoordinatorChannel.from(input, WatcherType.REALTIME, String.valueOf(processId), msg.toJson()));
        }
        if (input.getWatcherOption().isFallbackPolling()) {
            logger().warn("Fallback to create polling watcher due to unable to create realtime watcher. Error: {}",
                          msg.getError().toJson());
            return addPollingWatcher(input);
        }
        return Single.error(new CarlException(msg.getError().getCode(), msg.getError().getMessage()));
    }

}
