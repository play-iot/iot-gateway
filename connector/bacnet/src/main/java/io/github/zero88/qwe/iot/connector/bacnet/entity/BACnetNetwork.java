package io.github.zero88.qwe.iot.connector.bacnet.entity;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.entity.HasObjectType;
import io.github.zero88.qwe.iot.data.entity.INetwork;
import io.github.zero88.qwe.protocol.CommunicationProtocol;
import io.github.zero88.qwe.protocol.network.Ethernet;
import io.github.zero88.utils.Strings;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Getter
@FieldNameConstants
@Accessors(fluent = true)
public abstract class BACnetNetwork implements HasObjectType<String>, BACnetEntity<String>, INetwork<String> {

    @JsonProperty(Fields.type)
    private final String type;
    @JsonProperty(Fields.label)
    private final String label;

    BACnetNetwork(String type, String label) {
        this.type = type;
        this.label = label;
    }

    public static BACnetNetwork factory(@NonNull JsonObject data) {
        String type = (String) data.remove("type");
        if (Strings.isBlank(type) || BACnetIP.TYPE.equals(type)) {
            return JsonData.convert(data, BACnetIP.class);
        }
        if (BACnetMSTP.TYPE.equals(type)) {
            return JsonData.convert(data, BACnetMSTP.class);
        }
        throw new IllegalArgumentException(
            "Not support BACnet network type " + type + ". Only BACnet " + BACnetIP.TYPE + " or BACnet " +
            BACnetMSTP.TYPE);
    }

    public static BACnetNetwork fromProtocol(@NonNull CommunicationProtocol protocol) {
        if (protocol instanceof Ethernet) {
            return BACnetIP.from((Ethernet) protocol);
        }
        throw new IllegalArgumentException(
            "Unsupported protocol " + protocol.identifier() + " with type" + protocol.type());
    }

    public abstract @NonNull CommunicationProtocol toProtocol();

    @Override
    public @NonNull String key() {
        return toProtocol().identifier();
    }

    @SuppressWarnings("unchecked")
    static abstract class BACnetNetworkBuilder<T extends BACnetNetwork, B extends BACnetNetworkBuilder> {

        String label;

        public B label(String label) {
            this.label = label;
            return (B) this;
        }

        public abstract T build();

    }

}
