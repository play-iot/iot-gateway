package io.github.zero88.qwe.iot.data.entity;

import io.github.zero88.qwe.iot.data.property.PointPresentValue;
import io.github.zero88.qwe.iot.data.property.PointPriorityArray;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldNameConstants
@Accessors(fluent = true)
public abstract class AbstractPointData<K> implements IPointData<K> {

    @JsonProperty(Fields.key)
    private final K key;
    @NonNull
    @JsonProperty(Fields.pointId)
    private final String pointId;
    @NonNull
    @JsonProperty(Fields.presentValue)
    private final PointPresentValue presentValue;
    @NonNull
    @JsonProperty(Fields.priorityValue)
    private final PointPriorityArray priorityValue;

}
