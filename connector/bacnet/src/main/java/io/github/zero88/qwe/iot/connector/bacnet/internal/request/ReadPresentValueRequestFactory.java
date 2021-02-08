package io.github.zero88.qwe.iot.connector.bacnet.internal.request;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.internal.ack.AckServiceHandler;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import lombok.NonNull;

public final class ReadPresentValueRequestFactory
    implements ConfirmedRequestFactory<ReadPropertyRequest, ReadPropertyAck, RequestData> {

    @Override
    public @NonNull RequestData convertData(@NonNull DiscoveryArguments args, @NonNull RequestData requestData) {
        return requestData;
    }

    @Override
    public @NonNull ReadPropertyRequest factory(@NonNull DiscoveryArguments args, @NonNull RequestData data) {
        return new ReadPropertyRequest(args.params().objectCode(), PropertyIdentifier.presentValue);
    }

    @Override
    public AckServiceHandler<ReadPropertyAck> handler() {
        return readPropertyAck -> BACnetJsonMixin.MAPPER.convertValue(readPropertyAck.getValue(), JsonObject.class);
    }

}
