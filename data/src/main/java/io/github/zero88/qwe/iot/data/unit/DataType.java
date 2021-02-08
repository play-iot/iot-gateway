package io.github.zero88.qwe.iot.data.unit;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import io.github.classgraph.ClassInfo;
import io.github.zero88.qwe.dto.EnumType;
import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Base;
import io.github.zero88.utils.Reflections.ReflectionClass;
import io.github.zero88.utils.Reflections.ReflectionField;
import io.github.zero88.utils.Strings;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.NonNull;

@JsonInclude(Include.NON_EMPTY)
@JsonSerialize(as = DataType.class)
public interface DataType extends EnumType, Cloneable {

    String SEP = "::";

    static Stream<DataType> available() {
        return ReflectionClass.stream(DataType.class.getPackage().getName(), DataTypeCategory.class,
                                      ClassInfo::isInterface)
                              .flatMap(clazz -> ReflectionField.streamConstants(clazz, InternalDataType.class));
    }

    @NonNull
    static DataType def() { return Base.NUMBER; }

    @NonNull
    static DataType factory(String dittoValue) {
        if (Strings.isBlank(dittoValue)) {
            return def();
        }
        final String[] split = dittoValue.split(SEP);
        return factory(split[0], split.length > 1 ? split[1] : null);
    }

    @NonNull
    static DataType factory(String type, String unit) {
        return factory(type, unit, Base.NUMBER.category(), null);
    }

    @NonNull
    @JsonCreator
    static DataType factory(@JsonProperty(value = "type") String type, @JsonProperty(value = "symbol") String unit,
                            @JsonProperty(value = "category") String category,
                            @JsonProperty(value = "alias") Map<String, String> alias) {
        final DataType dt = available().filter(t -> t.type().equalsIgnoreCase(type))
                                       .findAny()
                                       .orElseGet(() -> new NumberDataType(type, unit));
        if (dt instanceof BooleanDataType) {
            return new BooleanDataType((BooleanDataType) dt, UnitAlias.create(alias));
        }
        return new NumberDataType(dt).setCategory(category).setAlias(UnitAlias.create(alias));
    }

    @NonNull
    static DataType factory(@NonNull JsonObject dataType, UnitAlias label) {
        return factory(dataType.getString("type"), dataType.getString("symbol"), dataType.getString("category"),
                       null).setAlias(label);
    }

    static DataType clone(@NonNull DataType type) {
        if (type instanceof BooleanDataType) {
            return new BooleanDataType((BooleanDataType) type);
        }
        return new NumberDataType(type);
    }

    @NonNull
    @JsonProperty(value = "symbol")
    String unit();

    @NonNull
    @JsonProperty(value = "category")
    String category();

    @JsonProperty(value = "alias")
    UnitAlias alias();

    DataType setAlias(UnitAlias alias);

    /**
     * Try parse given data to double value
     *
     * @param data given data
     * @return double value
     */
    default Double parse(Object data) {
        if (Objects.isNull(data)) {
            return 0d;
        }
        if (data instanceof Number) {
            return ((Number) data).doubleValue();
        }
        if (data instanceof String) {
            return Double.valueOf(((String) data).replaceAll(unit(), ""));
        }
        return 0d;
    }

    /**
     * Decor value by alias or unit type
     *
     * @param value given value
     * @return display value in string
     */
    default @NonNull String display(Double value) {
        if (Objects.isNull(value)) {
            return "";
        }
        if (Objects.nonNull(alias())) {
            String label = alias().eval(value);
            if (Strings.isNotBlank(label)) {
                return label;
            }
        }
        if (Strings.isBlank(unit())) {
            return String.valueOf(value);
        }
        return value + " " + unit();
    }

    default Collection<String> alternatives() { return null; }

    @Override
    default JsonObject toJson() {
        final JsonObject json = EnumType.super.toJson();
        if (Objects.nonNull(alias())) {
            json.put("alias", alias().toJson());
        }
        return json;
    }

}
