package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;

public final class PriorityArraySerializer extends EncodableSerializer<PriorityArray> {

    PriorityArraySerializer() {
        super(PriorityArray.class);
    }

    @Override
    public void serialize(PriorityArray value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(PriorityArray value, JsonGenerator gen) throws IOException {
        gen.writeObject(IntStream.range(0, value.size()).boxed().collect(Collectors.toMap(i -> ++i, value::get)));
    }

}
