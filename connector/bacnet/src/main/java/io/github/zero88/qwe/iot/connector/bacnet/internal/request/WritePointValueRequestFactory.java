package io.github.zero88.qwe.iot.connector.bacnet.internal.request;

import java.util.Objects;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.iot.data.property.PointPresentValue;

import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.internal.ack.NoAck;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer.EncodableDeserializer;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import lombok.NonNull;

public final class WritePointValueRequestFactory
    implements ConfirmedRequestFactory<WritePropertyRequest, NoAck, PropertyValue> {

    @Override
    public @NonNull PropertyValue convertData(@NonNull DiscoveryArguments args, @NonNull RequestData requestData) {
        final PointPresentValue ppv = Objects.requireNonNull(PointPresentValue.from(requestData.body()),
                                                             "Point Value is null");
        final PropertyIdentifier pi = PropertyIdentifier.presentValue;
        final Encodable encodable = EncodableDeserializer.parse(args.params().objectCode(), pi,
                                                                ppv.getPointValue().getValue());
        if (Objects.isNull(encodable)) {
            throw new IllegalArgumentException("Unrecognized value");
        }
        return new PropertyValue(pi, null, encodable, new UnsignedInteger(ppv.getPriority()));
    }

    @Override
    public @NonNull WritePropertyRequest factory(@NonNull DiscoveryArguments args, @NonNull PropertyValue pv) {
        return new WritePropertyRequest(args.params().objectCode(), pv.getPropertyIdentifier(),
                                        pv.getPropertyArrayIndex(), pv.getValue(), pv.getPriority());
    }

}
