package io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;

public final class ErrorClassAndCodeSerializer extends EncodableSerializer<ErrorClassAndCode> {

    ErrorClassAndCodeSerializer() {
        super(ErrorClassAndCode.class);
    }

    @Override
    public void serialize(ErrorClassAndCode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("errorClass", value.getErrorClass());
        map.put("errorCode", value.getErrorCode());
        gen.writeObject(map);
    }

}
