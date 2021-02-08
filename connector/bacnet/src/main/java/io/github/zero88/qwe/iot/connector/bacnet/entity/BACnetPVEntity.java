package io.github.zero88.qwe.iot.connector.bacnet.entity;

import io.github.zero88.qwe.iot.connector.bacnet.converter.property.PriorityValueConverter;
import io.github.zero88.qwe.iot.data.entity.AbstractPointData;
import io.github.zero88.qwe.iot.data.property.PointPresentValue;
import io.github.zero88.qwe.iot.data.property.PointPriorityArray;

import io.github.zero88.qwe.iot.connector.bacnet.converter.property.PointValueConverter;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@Accessors(fluent = true)
public class BACnetPVEntity extends AbstractPointData<ObjectIdentifier> implements BACnetEntity<ObjectIdentifier> {

    public static BACnetPVEntity from(@NonNull BACnetPointEntity point) {
        final PriorityArray pa = point.mixin().<PriorityArray>getAndCast(PropertyIdentifier.priorityArray).orElse(null);
        final PointPriorityArray ppa = new PriorityValueConverter().serialize(pa);
        final PointPresentValue ppv = new PointValueConverter().serialize(point.mixin());
        final PointPresentValue highestValue = ppa.findHighestValue();
        final PointPresentValue finalPV = PointPresentValue.builder()
                                                           .priority(highestValue.getPriority())
                                                           .value(ppv.getPointValue().getValue())
                                                           .rawValue(ppv.getPointValue().getRawValue())
                                                           .build();
        return BACnetPVEntity.builder()
                             .key(point.key())
                             .pointId(ObjectIdentifierMixin.serialize(point.key()))
                             .presentValue(finalPV)
                             .priorityValue(ppa)
                             .build();
    }

}
