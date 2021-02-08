package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.AddressMixin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.Address;

public final class AddressSerializer extends EncodableSerializer<Address> {

    AddressSerializer() {
        super(Address.class);
    }

    @Override
    public void serialize(Address value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(AddressMixin.create(value));
    }

}
