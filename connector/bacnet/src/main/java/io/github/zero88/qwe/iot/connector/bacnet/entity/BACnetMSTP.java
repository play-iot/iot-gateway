package io.github.zero88.qwe.iot.connector.bacnet.entity;

import io.github.zero88.qwe.protocol.serial.SerialPortProtocol;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = BACnetMSTP.Builder.class)
public final class BACnetMSTP extends BACnetNetwork {

    public static final String TYPE = "MSTP";
    private final int baud;
    private final int parity;
    private final int buffer;

    private BACnetMSTP(String name, int baud, int parity, int buffer) {
        super(TYPE, name);
        this.baud = baud;
        this.parity = parity;
        this.buffer = buffer;
    }

    @Override
    public @NonNull SerialPortProtocol toProtocol() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends BACnetNetworkBuilder<BACnetMSTP, BACnetMSTP.Builder> {

        public BACnetMSTP build() {
            return new BACnetMSTP(label, baud, parity, buffer);
        }

    }

}
