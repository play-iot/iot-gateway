package io.github.zero88.qwe.iot.data.unified;

import java.util.UUID;

import io.github.zero88.qwe.iot.data.entity.INetwork;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@Accessors(fluent = true)
public class UnifiedNetwork implements INetwork<UUID>, UnifiedIoTEntity {

    @NonNull
    @Default
    private final UUID key = UUID.randomUUID();
    @NonNull
    private final String type;
    @Default
    private final ParticularData<INetwork> particularData = new ParticularData<>(INetwork.class);

}
