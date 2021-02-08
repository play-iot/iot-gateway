package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.AddressMixin;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.ObjectIdentifierMixin;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.constructed.AddressBinding;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.NonNull;

public final class AddressBindingDeserializer extends BaseTypeDeserializer<AddressBinding> {

    AddressBindingDeserializer() {
        super(AddressBinding.class);
    }

    @Override
    public AddressBinding parse(@NonNull JsonObject value) {
        ObjectIdentifier deviceObjectIdentifier = ObjectIdentifierMixin.deserialize(
            value.getString("device-object-identifier", ""));
        AddressMixin addressMixin = AddressMixin.create(value.getJsonObject("device-address"));
        return new AddressBinding(deviceObjectIdentifier, addressMixin.unwrap());
    }

}
