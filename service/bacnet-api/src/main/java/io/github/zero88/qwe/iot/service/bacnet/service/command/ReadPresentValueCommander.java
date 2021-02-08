package io.github.zero88.qwe.iot.service.bacnet.service.command;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.ReadPresentValueRequestFactory;
import io.github.zero88.qwe.iot.service.bacnet.service.AbstractBACnetService;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;

import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import lombok.NonNull;

public final class ReadPresentValueCommander extends AbstractBACnetService implements BACnetReadCommander {

    protected ReadPresentValueCommander(@NonNull SharedDataLocalProxy sharedData) {
        super(sharedData);
    }

    @Override
    @EventContractor(action = "SEND", returnType = Single.class)
    public Single<JsonObject> send(@NonNull RequestData requestData) {
        final DiscoveryArguments args = createDiscoveryArgs(requestData, level());
        final BACnetDevice device = getLocalDeviceFromCache(args);
        return device.send(EventAction.SEND, args, requestData, new ReadPresentValueRequestFactory())
                     .map(JsonData::toJson);
    }

    @Override
    public @NonNull DiscoveryLevel level() {
        return DiscoveryLevel.OBJECT;
    }

    @Override
    public @NonNull String subFunction() {
        return BACnetJsonMixin.standardizeKey(PropertyIdentifier.presentValue.toString());
    }

}
