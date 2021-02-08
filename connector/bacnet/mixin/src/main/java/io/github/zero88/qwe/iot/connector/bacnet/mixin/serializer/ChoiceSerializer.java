package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.Choice;

public final class ChoiceSerializer extends EncodableSerializer<Choice> {

    ChoiceSerializer() {
        super(Choice.class);
    }

    @Override
    public void serialize(Choice value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(Choice value, JsonGenerator gen) throws IOException {
        gen.writeObject(value.getDatum());
    }

}
