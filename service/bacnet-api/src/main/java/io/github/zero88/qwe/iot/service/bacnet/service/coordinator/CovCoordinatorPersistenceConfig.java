package io.github.zero88.qwe.iot.service.bacnet.service.coordinator;

import io.github.zero88.qwe.dto.JsonData;
import io.vertx.core.shareddata.Shareable;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public final class CovCoordinatorPersistenceConfig implements JsonData, Shareable {

    @Default
    private final String type = "json";
    @Default
    private final String file = "bacnet-cov-coordinator.json";
    @Default
    //TODO switch to remote service name rather than address
    private final String serviceName = "bacnet.storage.json";

    public static CovCoordinatorPersistenceConfig def() {
        return CovCoordinatorPersistenceConfig.builder().build();
    }

    @Override
    public Shareable copy() {
        return JsonData.from(toJson(), CovCoordinatorPersistenceConfig.class);
    }

}
