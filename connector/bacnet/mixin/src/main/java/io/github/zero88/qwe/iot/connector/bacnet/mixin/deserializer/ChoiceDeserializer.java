package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.constructed.Choice;

import lombok.NonNull;

public final class ChoiceDeserializer implements EncodableDeserializer<Choice, String> {

    @Override
    public @NonNull Class<Choice> encodableClass() {
        return Choice.class;
    }

    @Override
    public @NonNull Class<String> javaClass() {
        return String.class;
    }

    @Override
    public Choice parse(@NonNull String value) {
        //TODO implement it
        return null;
    }

}
