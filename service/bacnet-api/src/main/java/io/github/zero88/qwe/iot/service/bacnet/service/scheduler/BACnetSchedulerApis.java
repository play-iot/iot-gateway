package io.github.zero88.qwe.iot.service.bacnet.service.scheduler;

import java.util.Arrays;
import java.util.List;

import org.quartz.Scheduler;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.iot.service.bacnet.cache.BACnetCacheInitializer;
import io.github.zero88.qwe.micro.ServiceDiscoveryInvoker;
import io.github.zero88.qwe.micro.http.ActionMethodMapping;
import io.github.zero88.qwe.micro.http.EventMethodDefinition;
import io.github.zero88.qwe.scheduler.service.SchedulerConverterHelper;
import io.github.zero88.qwe.scheduler.service.SchedulerRegisterService;
import io.reactivex.Single;
import io.vertx.servicediscovery.Record;

import lombok.NonNull;

//TODO It should belongs to scheduler service
public class BACnetSchedulerApis extends SchedulerRegisterService {

    protected BACnetSchedulerApis(@NonNull Scheduler scheduler, @NonNull SharedDataLocalProxy sharedData,
                                  @NonNull SchedulerConverterHelper converterHelper) {
        super(scheduler, sharedData, converterHelper);
    }

    public static Single<Record> registerApis(@NonNull ServiceDiscoveryInvoker invoker,
                                              @NonNull SharedDataLocalProxy sharedData) {
        final List<EventAction> available = Arrays.asList(EventAction.GET_LIST, EventAction.GET_ONE, EventAction.CREATE,
                                                          EventAction.REMOVE);
        final ActionMethodMapping m = ActionMethodMapping.byCRUD(available);
        final String serviceName = sharedData.getData(BACnetCacheInitializer.SCHEDULER_SERVICE_NAME);
        return invoker.addEventMessageRecord(serviceName, "bacnet.scheduler.register",
                                             EventMethodDefinition.create("/scheduler/:jobKey/", "triggerKey", m));
    }

}
