package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import io.github.zero88.qwe.protocol.network.IpNetwork;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.serotonin.bacnet4j.npdu.NetworkUtils;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.util.BACnetUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Getter
@FieldNameConstants
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AddressMixin implements EncodableMixin<Address> {

    @NonNull
    private final String type;
    private final int networkNumber;
    @NonNull
    private final String hostAddress;
    @NonNull
    private final String macAddress;

    public static AddressMixin create(@NonNull Address address) {
        final OctetString mac = address.getMacAddress();
        return new AddressMixin(mac.getLength() == 1 ? "MSTP" : "IP", address.getNetworkNumber().intValue(),
                                mac.getDescription(), IpNetwork.mac(mac.getBytes()));
    }

    @JsonCreator
    public static AddressMixin create(@NonNull JsonObject address) {
        return new AddressMixin(address.getString(Fields.type, "IP"), address.getInteger(Fields.networkNumber),
                                address.getString(Fields.hostAddress), address.getString(Fields.macAddress));
    }

    @Override
    public Address unwrap() {
        if ("IP".equals(type) && !hostAddress.contains(":")) {
            return new Address(networkNumber, BACnetUtils.dottedStringToBytes(hostAddress));
        }
        return new Address(networkNumber, NetworkUtils.toOctetString(hostAddress));
    }

}
