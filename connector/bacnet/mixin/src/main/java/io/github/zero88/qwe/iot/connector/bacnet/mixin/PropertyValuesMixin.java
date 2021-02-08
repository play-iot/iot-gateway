package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.adjuster.PriorityValuesAdjuster;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.serializer.EncodableSerializer;
import io.github.zero88.utils.Functions;
import io.reactivex.annotations.Nullable;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.deserializer.EncodableDeserializer;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.error.ErrorClassAndCode;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyValues;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @see PropertyValues
 */
@Setter(value = AccessLevel.PRIVATE)
@Accessors(chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyValuesMixin implements BACnetJsonMixin {

    private final Map<PropertyIdentifier, Encodable> values;
    private ObjectIdentifier objectId;

    @JsonCreator
    public static PropertyValuesMixin create(JsonObject properties) {
        final Function<Entry<String, Object>, Entry<PropertyIdentifier, Object>> converter = entry -> new SimpleEntry<>(
            Functions.getIfThrow(() -> PropertyIdentifier.forName(entry.getKey())).orElse(null), entry.getValue());
        final Function<Entry<PropertyIdentifier, Object>, Encodable> parser
            = entry -> EncodableDeserializer.parseLenient(entry.getKey(), entry.getValue());
        return new PropertyValuesMixin(Optional.ofNullable(properties)
                                               .orElse(new JsonObject())
                                               .stream()
                                               .map(converter)
                                               .filter(entry -> Objects.nonNull(entry.getKey()))
                                               .collect(Collectors.toMap(Entry::getKey, parser))).tweak();
    }

    public static PropertyValuesMixin create(@NonNull ObjectIdentifier objId, @NonNull PropertyValues values,
                                             boolean includeError) {
        final Map<PropertyIdentifier, Encodable> map = new HashMap<>();
        values.forEach(opr -> {
            if (!opr.getObjectIdentifier().equals(objId) ||
                PropertyIdentifier.propertyList.equals(opr.getPropertyIdentifier())) {
                return;
            }
            final Encodable value = values.getNoErrorCheck(opr);
            if (value instanceof ErrorClassAndCode && !includeError) {
                return;
            }
            map.put(opr.getPropertyIdentifier(), value);
        });
        return new PropertyValuesMixin(map).setObjectId(objId);
    }

    /**
     * Get encodable value
     *
     * @param propertyIdentifier given property identifier
     * @return encodable value
     */
    @Nullable
    public Encodable get(@NonNull PropertyIdentifier propertyIdentifier) {
        return values.get(propertyIdentifier);
    }

    /**
     * Get and cast encodable value
     *
     * @param propertyIdentifier given property identifier
     * @param <E>                Type of {@code Encodable}
     * @return optional encodable. Empty in case cannot cast
     */
    @SuppressWarnings("unchecked")
    public <E extends Encodable> Optional<E> getAndCast(@NonNull PropertyIdentifier propertyIdentifier) {
        return Functions.getIfThrow(() -> (E) values.get(propertyIdentifier));
    }

    /**
     * Encode property to plain java type
     *
     * @param propertyIdentifier given property identifier
     * @param <T>                Type of expected value
     * @return value
     */
    @Nullable
    public <T> T encode(@NonNull PropertyIdentifier propertyIdentifier) {
        return EncodableSerializer.encode(getAndCast(propertyIdentifier).orElse(null));
    }

    public PropertyValuesMixin viewByProperties(@NonNull Collection<PropertyIdentifier> properties) {
        return new PropertyValuesMixin(properties.stream().collect(Collectors.toMap(pi -> pi, values::get)));
    }

    public PropertyValuesMixin viewWithoutProperties(@NonNull Collection<PropertyIdentifier> properties) {
        return new PropertyValuesMixin(values.entrySet()
                                             .stream()
                                             .filter(entry -> !properties.contains(entry.getKey()))
                                             .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
    }

    public ObjectIdentifier getObjectId() {
        return Optional.ofNullable(objectId)
                       .orElseGet(
                           () -> (ObjectIdentifier) getAndCast(PropertyIdentifier.objectIdentifier).orElse(null));
    }

    @Override
    public JsonObject toJson() {
        return getMapper().convertValue(values, JsonObject.class);
    }

    private PropertyValuesMixin tweak() {
        values.computeIfPresent(PropertyIdentifier.priorityArray,
                                (pId, arrayValues) -> new PriorityValuesAdjuster().apply(
                                    values.get(PropertyIdentifier.presentValue), (PriorityArray) arrayValues));
        return this.setObjectId((ObjectIdentifier) getAndCast(PropertyIdentifier.objectIdentifier).orElse(null));
    }

    public static final class PropertyValuesSerializer extends StdSerializer<PropertyValuesMixin> {

        PropertyValuesSerializer() {
            super(PropertyValuesMixin.class);
        }

        @Override
        public void serialize(PropertyValuesMixin value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
            gen.writeObject(value.toJson());
        }

    }

}
