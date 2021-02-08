package io.github.zero88.qwe.iot.connector.bacnet;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import io.github.zero88.qwe.component.HasSharedData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryArguments;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryOptions;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.ConfirmedRequestFactory;
import io.github.zero88.qwe.iot.connector.bacnet.internal.request.RemoteDeviceScanner;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.PropertyValuesMixin;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;

/**
 * The interface BACnet Device.
 *
 * @since 1.0.0
 */
public interface BACnetDevice extends HasSharedData {

    /**
     * The constant CONFIG_KEY in cache.
     */
    String CONFIG_KEY = "BACNET_CONFIG";

    /**
     * Gets local device config.
     *
     * @return the local device config
     * @see BACnetConfig
     * @since 1.0.0
     */
    @NonNull BACnetConfig config();

    /**
     * Gets communication protocol.
     *
     * @return the communication protocol
     * @see CommunicationProtocol
     * @since 1.0.0
     */
    @NonNull CommunicationProtocol protocol();

    /**
     * Gets local device.
     *
     * @return the local device
     * @see LocalDevice
     * @since 1.0.0
     */
    @NonNull LocalDevice localDevice();

    /**
     * Add listeners to BACnet Device.
     *
     * @param listeners the listeners
     * @return a reference to this, so the API can be used fluently
     * @since 1.0.0
     */
    @NonNull BACnetDevice addListeners(@NonNull List<DeviceEventListener> listeners);

    /**
     * Add listener to BACnet Device.
     *
     * @param listeners the listeners
     * @return a reference to this, so the API can be used fluently
     * @since 1.0.0
     */
    default @NonNull BACnetDevice addListeners(DeviceEventListener... listeners) {
        return addListeners(Arrays.asList(listeners));
    }

    @Nullable
    default <T extends DeviceEventListener> T lookupListener(@NonNull Class<T> listenerClass) {
        return lookupListener(listenerClass, () -> null);
    }

    <T extends DeviceEventListener> T lookupListener(@NonNull Class<T> listenerClass, Supplier<T> supplier);

    /**
     * Async start BACnet device.
     *
     * @return a reference to this, so the API can be used fluently
     * @since 1.0.0
     */
    @NonNull BACnetDevice asyncStart();

    /**
     * Stop BACnet device.
     *
     * @return a reference to this, so the API can be used fluently
     * @since 1.0.0
     */
    @NonNull Single<BACnetDevice> stop();

    /**
     * Scan remote devices in {@code BACnet} network
     *
     * @param options the options
     * @return the remote devices
     * @see DiscoveryOptions
     * @see RemoteDeviceScanner
     * @since 1.0.0
     */
    @NonNull Single<RemoteDeviceScanner> scanRemoteDevices(@NonNull DiscoveryOptions options);

    /**
     * Discover remote device.
     *
     * @param arguments the discovery arguments
     * @return the remote device
     * @see DiscoveryArguments
     * @see RemoteDevice
     * @since 1.0.0
     */
    @NonNull Single<RemoteDevice> discoverRemoteDevice(@NonNull DiscoveryArguments arguments);

    /**
     * Discover remote object
     *
     * @param arguments the discovery arguments
     * @return the property values of object
     * @see DiscoveryArguments
     * @see PropertyValuesMixin
     * @since 1.0.0
     */
    @NonNull Single<PropertyValuesMixin> discoverRemoteObject(@NonNull DiscoveryArguments arguments);

    /**
     * Parse remote object
     *
     * @param remoteDevice remote device
     * @param objId        object id
     * @param detail       should be detail
     * @param includeError should include error
     * @return the property values of object
     * @see PropertyValuesMixin
     * @since 1.0.0
     */
    @NonNull Single<PropertyValuesMixin> parseRemoteObject(@NonNull RemoteDevice remoteDevice,
                                                           @NonNull ObjectIdentifier objId, boolean detail,
                                                           boolean includeError);

    /**
     * Send BACnet request
     *
     * @param action      Event action
     * @param args        Discovery request arguments
     * @param requestData Request data
     * @param factory     BACnet request
     * @return json result
     * @see DiscoveryArguments
     * @see ConfirmedRequestFactory
     */
    @NonNull <C extends ConfirmedRequestService, A extends AcknowledgementService, D> Single<EventMessage> send(
        @NonNull EventAction action, @NonNull DiscoveryArguments args, @NonNull RequestData requestData,
        @NonNull ConfirmedRequestFactory<C, A, D> factory);

}
