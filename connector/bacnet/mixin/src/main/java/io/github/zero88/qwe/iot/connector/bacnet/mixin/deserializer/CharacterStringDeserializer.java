package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import com.serotonin.bacnet4j.type.primitive.CharacterString;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class CharacterStringDeserializer implements EncodableDeserializer<CharacterString, String> {

    @Override
    public @NonNull Class<CharacterString> encodableClass() {
        return CharacterString.class;
    }

    @NonNull
    @Override
    public Class<String> javaClass() {
        return String.class;
    }

    @Override
    public CharacterString parse(@NonNull String value) {
        return new CharacterString(value);
    }

}
