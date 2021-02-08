package io.github.zero88.qwe.iot.data.unified;

import java.util.UUID;

import io.github.zero88.qwe.iot.data.entity.AbstractDevice;
import io.github.zero88.qwe.iot.data.entity.IDevice;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@Accessors(fluent = true)
public class UnifiedDevice extends AbstractDevice<UUID> implements UnifiedIoTEntity {

    @NonNull
    @Default
    private final ParticularData<IDevice> particularData = new ParticularData<>(IDevice.class);

}
