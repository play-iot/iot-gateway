package io.github.zero88.qwe.iot.connector.bacnet.internal.request;

import java.util.stream.Collectors;

import io.github.zero88.qwe.dto.msg.RequestData;

import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.internal.ack.AckServiceHandler;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.AccessResultMixin;

import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import lombok.NonNull;

public final class ReadPointValueRequestFactory
    implements ConfirmedRequestFactory<ReadPropertyMultipleRequest, ReadPropertyMultipleAck, RequestData> {

    @Override
    public @NonNull RequestData convertData(@NonNull DiscoveryArguments args, @NonNull RequestData requestData) {
        return requestData;
    }

    @Override
    public @NonNull ReadPropertyMultipleRequest factory(@NonNull DiscoveryArguments args, @NonNull RequestData data) {
        final SequenceOf<ReadAccessSpecification> specs = new SequenceOf<>();
        specs.add(new ReadAccessSpecification(args.params().objectCode(),
                                              new SequenceOf<>(new PropertyReference(PropertyIdentifier.presentValue),
                                                               new PropertyReference(
                                                                   PropertyIdentifier.priorityArray))));
        return new ReadPropertyMultipleRequest(specs);
    }

    @Override
    public AckServiceHandler<ReadPropertyMultipleAck> handler() {
        return readPropertyAck -> AccessResultMixin.create(readPropertyAck.getListOfReadAccessResults()
                                                                          .getValues()
                                                                          .stream()
                                                                          .flatMap(rar -> rar.getListOfResults()
                                                                                             .getValues()
                                                                                             .stream())
                                                                          .collect(Collectors.toList())).toJson();
    }

}
