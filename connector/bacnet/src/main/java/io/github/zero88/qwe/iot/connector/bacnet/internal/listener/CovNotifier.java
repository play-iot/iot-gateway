package io.github.zero88.qwe.iot.connector.bacnet.internal.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.event.Waybill;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetDevice;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryParams;
import io.github.zero88.qwe.iot.connector.bacnet.dto.CovOutput;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @see <a href="https://store.chipkin.com/articles/bacnet-what-is-the-bacnet-change-of-value-cov">COV</a>
 */
@Slf4j
@RequiredArgsConstructor
public final class CovNotifier extends DeviceEventAdapter implements DeviceEventListener {

    private final BACnetDevice device;
    private final Map<String, Waybill> dispatchers = new HashMap<>();

    @Override
    public void covNotificationReceived(final UnsignedInteger subscriberProcessIdentifier,
                                        final ObjectIdentifier initiatingDevice,
                                        final ObjectIdentifier monitoredObjectIdentifier,
                                        final UnsignedInteger timeRemaining,
                                        final SequenceOf<PropertyValue> listOfValues) {
        final String key = DiscoveryParams.builder()
                                          .networkId(device.protocol().identifier())
                                          .deviceInstance(initiatingDevice.getInstanceNumber())
                                          .objectCode(ObjectIdentifierMixin.serialize(monitoredObjectIdentifier))
                                          .build().buildKey(DiscoveryLevel.OBJECT);
        final long time = Optional.ofNullable(timeRemaining).map(UnsignedInteger::longValue).orElse(-1L);
        final Waybill dispatcher = dispatchers.get(key);
        if (Objects.isNull(dispatcher)) {
            if (log.isDebugEnabled()) {
                log.debug("COV of '{}' notification received, time remaining: '{}s'", key, time);
                convertValue(listOfValues);
            }
            return;
        }
        log.info("COV of '{}' notification received, time remaining: '{}s'", key, time);
        final JsonArray convertValue = convertValue(listOfValues);
        final CovOutput cov = CovOutput.builder()
                                       .key(key)
                                       .cov(convertValue)
                                       .any(new JsonObject().put("timeRemaining", time))
                                       .build();
        EventbusClient.create(device.sharedData())
                      .publish(dispatcher.getAddress(), EventMessage.initial(dispatcher.getAction(), cov.toJson()));
    }

    private JsonArray convertValue(SequenceOf<PropertyValue> listOfValues) {
        final JsonArray convertValue = BACnetJsonMixin.MAPPER.convertValue(listOfValues, JsonArray.class);
        log.debug("Value: {}", convertValue);
        return convertValue;
    }

    public void addDispatcher(@NonNull EventMessage subscribeCOVResult, @NonNull DiscoveryArguments args,
                              @NonNull RequestData requestData) {
        dispatchers.put(args.key(), Waybill.from(requestData.body()));
    }

    public void removeDispatcher(@NonNull EventMessage subscribeCOVResult, @NonNull DiscoveryArguments args,
                                 @NonNull RequestData requestData) {
        dispatchers.remove(args.key());
    }

}
