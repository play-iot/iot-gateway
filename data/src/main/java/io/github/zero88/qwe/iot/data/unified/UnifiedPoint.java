package io.github.zero88.qwe.iot.data.unified;

import java.util.UUID;

import io.github.zero88.qwe.iot.data.entity.AbstractPoint;
import io.github.zero88.qwe.iot.data.entity.IPoint;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@Accessors(fluent = true)
public class UnifiedPoint extends AbstractPoint<UUID> implements UnifiedIoTEntity {

    @Default
    private final ParticularData<IPoint> particularData = new ParticularData<>(IPoint.class);

}
