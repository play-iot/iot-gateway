package io.github.zero88.qwe.iot.service.bacnet;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetConfig;
import io.github.zero88.qwe.iot.service.bacnet.service.coordinator.CovCoordinatorPersistenceConfig;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class BACnetServiceConfig extends BACnetConfig {

    @NonNull
    @Default
    private final String gatewayAddress = "gateway.index";
    @NonNull
    @Default
    private final String schedulerServiceName = "bacnet-scheduler";

    @Default
    private final CovCoordinatorPersistenceConfig covCoordinatorPersistence = CovCoordinatorPersistenceConfig.def();

    @Override
    public int getVendorId() {
        return 1173;
    }

    @Override
    public String getVendorName() {
        return "QWE iO Operations Pty Ltd";
    }

    @Override
    public String getModelName() {
        return super.getModelName().contains("QWE") ? "Rubix" : super.getModelName();
    }

}
