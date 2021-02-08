package io.github.zero88.qwe.iot.connector.bacnet.converter.property;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.zero88.qwe.iot.data.converter.IoTPropertyConverter;
import io.github.zero88.qwe.iot.data.property.PointPriorityArray;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetProtocol;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import lombok.NonNull;

//TODO implement it
public class PriorityValueConverter implements IoTPropertyConverter<PointPriorityArray, PriorityArray>, BACnetProtocol {

    static final List<PropertyIdentifier> DATA_PROPS = Arrays.asList(PropertyIdentifier.presentValue,
                                                                     PropertyIdentifier.priorityArray);

    @Override
    public PointPriorityArray serialize(PriorityArray object) {
        if (Objects.isNull(object)) {
            return new PointPriorityArray();
        }
        return new PointPriorityArray();
    }

    @Override
    public PriorityArray deserialize(PointPriorityArray concept) {
        if (Objects.isNull(concept)) {
            return new PriorityArray();
        }
        return null;
    }

    @Override
    public @NonNull Class<PointPriorityArray> fromType() {
        return PointPriorityArray.class;
    }

    @Override
    public @NonNull Class<PriorityArray> toType() {
        return PriorityArray.class;
    }

}
