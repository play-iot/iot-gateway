package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;

import lombok.NonNull;

public final class ErrorClassAndCodeDeserializer implements EncodableDeserializer<ErrorClassAndCode, JsonObject> {

    @Override
    public @NonNull Class<ErrorClassAndCode> encodableClass() {
        return ErrorClassAndCode.class;
    }

    @Override
    public @NonNull Class<JsonObject> javaClass() {
        return JsonObject.class;
    }

    @Override
    public ErrorClassAndCode parse(@NonNull JsonObject value) {
        ErrorClass errorClass = ErrorClass.forName(value.getString("errorClass"));
        ErrorCode errorCode = ErrorCode.forName(value.getString("errorCode"));
        return new ErrorClassAndCode(errorClass, errorCode);
    }

}
