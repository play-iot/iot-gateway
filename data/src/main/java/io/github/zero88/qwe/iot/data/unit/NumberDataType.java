package io.github.zero88.qwe.iot.data.unit;

import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Base;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NumberDataType implements InternalDataType, DataType {

    @Getter
    @Include
    private final String type;
    private final String unit;
    private String category;
    private UnitAlias alias;

    NumberDataType() {
        this("number", null);
    }

    NumberDataType(String type, String unit) {
        this(type, unit, Base.TYPE, null);
    }

    NumberDataType(String type, String unit, String category) {
        this(type, unit, category, null);
    }

    NumberDataType(DataType dt) {
        this.type = dt.type();
        this.unit = dt.unit();
        this.category = dt.category();
        this.alias = dt.alias();
    }

    @Override
    public String type() { return type; }

    @Override
    public final String unit() { return unit; }

    @Override
    public @NonNull String category() { return category; }

    @Override
    public UnitAlias alias() { return alias; }

    @Override
    public DataType setAlias(UnitAlias alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public InternalDataType setCategory(String category) {
        this.category = category;
        return this;
    }

}
