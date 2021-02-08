package io.github.zero88.qwe.iot.connector.rpc.scheduler;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.exceptions.ServiceException;
import io.github.zero88.qwe.iot.connector.rpc.RpcClient;
import io.github.zero88.qwe.scheduler.service.SchedulerRegisterArgs;
import io.github.zero88.qwe.scheduler.service.SchedulerRegisterResp;
import io.reactivex.Single;

import lombok.NonNull;

public interface SchedulerClient extends RpcClient {

    @Override
    default @NonNull String function() {
        return "schedule";
    }

    /**
     * A scheduler service name
     *
     * @return scheduler service name
     */
    @Override
    @NonNull String destination();

    @Override
    default boolean throwIfResponseError() {
        return true;
    }

    default Single<SchedulerRegisterResp> execute(@NonNull EventAction action, @NonNull SchedulerRegisterArgs args,
                                                  @NonNull String schedulerIdentifier) {
        return this.execute(action, args.toJson())
                   .map(resp -> JsonData.from(resp, SchedulerRegisterResp.class))
                   .onErrorResumeNext(
                       t -> Single.error(new ServiceException("Unable to " + action + " " + schedulerIdentifier, t)));
    }

}
