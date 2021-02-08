package io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer;

import io.github.zero88.utils.Functions;
import io.vertx.core.json.JsonObject;

import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PriorityArrayDeserializer
    implements EncodableDeserializer<PriorityArray, JsonObject>, NonRegistryDeserializer {

    @NonNull
    private final PropertyTypeDefinition itemDefinition;
    @NonNull
    private final PriorityValueDeserializer itemDeserializer = new PriorityValueDeserializer();

    @Override
    public @NonNull Class<PriorityArray> encodableClass() {
        return PriorityArray.class;
    }

    @Override
    public @NonNull Class<JsonObject> javaClass() {
        return JsonObject.class;
    }

    @Override
    public PriorityArray parse(@NonNull JsonObject value) {
        final PriorityArray array = new PriorityArray();
        value.stream()
             .filter(entry -> Functions.getIfThrow(() -> Functions.toInt().apply(entry.getKey())).isPresent())
             .forEach(entry -> array.setBase1(Functions.toInt().apply(entry.getKey()), parseValue(entry.getValue())));
        return array;
    }

    private PriorityValue parseValue(Object value) {
        return (PriorityValue) Functions.getIfThrow(
            () -> EncodableDeserializer.parse(value, itemDefinition, itemDeserializer)).orElse(null);
    }

}
