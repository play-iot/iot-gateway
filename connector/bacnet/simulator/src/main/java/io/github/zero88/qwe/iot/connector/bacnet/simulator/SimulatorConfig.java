package io.github.zero88.qwe.iot.connector.bacnet.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.zero88.qwe.IConfig;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetConfig;
import io.github.zero88.qwe.iot.connector.bacnet.entity.BACnetNetwork;
import io.github.zero88.utils.Functions;
import io.github.zero88.utils.Strings;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
final class SimulatorConfig extends BACnetConfig {

    @JsonProperty(value = PredefinedNetwork.KEY)
    private final PredefinedNetwork networks = new PredefinedNetwork();

    @Override
    protected int maxDeviceId() {
        return 91000;
    }

    @Override
    protected int minDeviceId() {
        return MAX_DEVICE_ID + 1;
    }

    PredefinedNetwork getNetworks() {
        return (PredefinedNetwork) networks.copy();
    }

    public static final class PredefinedNetwork extends HashMap<String, JsonObject> implements IConfig {

        static final String KEY = "__networks__";

        @Override
        public String key() {
            return KEY;
        }

        @Override
        public Class<? extends IConfig> parent() {
            return BACnetConfig.class;
        }

        List<BACnetNetwork> toNetworks() {
            return entrySet().stream()
                             .filter(entry -> Objects.nonNull(entry.getValue()) && Strings.isNotBlank(entry.getKey()))
                             .map(entry -> entry.getValue().copy().put("label", entry.getKey()))
                             .map(data -> Functions.getIfThrow(() -> BACnetNetwork.factory(data)))
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .collect(Collectors.toList());
        }

    }

}
