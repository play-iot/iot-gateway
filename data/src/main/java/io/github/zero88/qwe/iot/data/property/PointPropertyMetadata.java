package io.github.zero88.qwe.iot.data.property;

import io.github.zero88.qwe.iot.data.IoTProperty;
import io.github.zero88.qwe.iot.data.enums.PointKind;
import io.github.zero88.qwe.iot.data.enums.PointType;
import io.github.zero88.qwe.iot.data.enums.TransducerCategory;
import io.github.zero88.qwe.iot.data.enums.TransducerType;
import io.github.zero88.qwe.iot.data.unit.DataType;
import io.github.zero88.qwe.iot.data.unit.UnitAlias;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@Builder(builderClassName = "Builder")
public class PointPropertyMetadata implements IoTProperty {

    private final PointType pointType;
    private final PointKind pointKind;
    private final TransducerType transducerType;
    private final TransducerCategory transducerCategory;
    private final DataType unit;
    private final UnitAlias unitAlias;

}
