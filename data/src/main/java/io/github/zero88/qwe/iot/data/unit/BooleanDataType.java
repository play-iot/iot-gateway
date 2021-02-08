package io.github.zero88.qwe.iot.data.unit;

import java.util.Objects;
import java.util.Optional;

import io.github.zero88.qwe.iot.data.unit.DataTypeCategory.Base;
import io.github.zero88.utils.Strings;

import lombok.NonNull;

public final class BooleanDataType extends NumberDataType {

    BooleanDataType() {
        super("bool", null, Base.TYPE);
    }

    BooleanDataType(@NonNull BooleanDataType dt, UnitAlias unitAlias) {
        super(dt.type(), dt.unit(), dt.category(), Optional.ofNullable(unitAlias).orElse(dt.alias()));
    }

    BooleanDataType(@NonNull BooleanDataType dt) {
        super(dt.type(), dt.unit(), dt.category(), dt.alias());
    }

    @Override
    public Double parse(Object data) {
        if (Objects.isNull(data)) {
            return 0d;
        }
        if (data instanceof Number) {
            return ((Number) data).doubleValue() > 0 ? 1d : 0d;
        }
        if (data instanceof Boolean) {
            return Boolean.TRUE == data ? 1d : 0d;
        }
        if (data instanceof String) {
            return Boolean.TRUE.equals(Boolean.valueOf((String) data)) ? 1d : 0d;
        }
        return 0d;
    }

    @Override
    public @NonNull String display(Double value) {
        Double val = Optional.ofNullable(value).orElse(0d);
        if (Objects.nonNull(alias())) {
            String label = alias().eval(val);
            if (Strings.isNotBlank(label)) {
                return label;
            }
        }
        return val > 0 ? "TRUE" : "FALSE";
    }

}
