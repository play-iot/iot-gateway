package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;

public final class PropertyValueSerializer extends EncodableSerializer<PropertyValue> {

    PropertyValueSerializer() {
        super(PropertyValue.class);
    }

    @Override
    public void serialize(PropertyValue value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        serializeIfAnyErrorFallback(this::serialize, value, gen);
    }

    private void serialize(PropertyValue value, JsonGenerator gen) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("propertyIdentifier", value.getPropertyIdentifier());
        map.put("priority", value.getPriority());
        map.put("arrayIndex", value.getPropertyArrayIndex());
        map.put("value", value.getValue());
        gen.writeObject(map);
    }

}
