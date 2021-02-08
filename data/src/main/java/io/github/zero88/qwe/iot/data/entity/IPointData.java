package io.github.zero88.qwe.iot.data.entity;

import io.github.zero88.qwe.iot.data.IoTEntity;
import io.github.zero88.qwe.iot.data.TimeseriesData;
import io.github.zero88.qwe.iot.data.property.PointPresentValue;
import io.github.zero88.qwe.iot.data.property.PointPriorityArray;

import lombok.NonNull;

public interface IPointData<K> extends IoTEntity<K>, TimeseriesData {

    /**
     * Retrieve a point identifier that point data belongs to
     *
     * @return point identifier
     */
    @NonNull String pointId();

    /**
     * Define present value
     *
     * @return present value
     * @see PointPresentValue
     */
    PointPresentValue presentValue();

    /**
     * Define point priority value
     *
     * @return point priority value
     * @see PointPriorityArray
     */
    PointPriorityArray priorityValue();

}
