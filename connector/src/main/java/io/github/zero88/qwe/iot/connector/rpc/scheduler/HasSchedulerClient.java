package io.github.zero88.qwe.iot.connector.rpc.scheduler;

import lombok.NonNull;

public interface HasSchedulerClient {

    @NonNull SchedulerClient schedulerService();

}
