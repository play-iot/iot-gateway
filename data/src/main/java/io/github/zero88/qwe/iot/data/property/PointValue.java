package io.github.zero88.qwe.iot.data.property;

import java.util.Optional;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.IoTProperty;
import io.github.zero88.qwe.iot.data.TimeseriesData;
import io.github.zero88.utils.Functions;
import io.vertx.core.json.JsonObject;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@FieldNameConstants
public final class PointValue implements JsonData, IoTProperty, TimeseriesData {

    private final String value;
    private final Double rawValue;

    public static PointValue from(@NonNull JsonObject json) {
        return PointValue.builder()
                         .rawValue(json.getDouble(PointValue.Fields.rawValue))
                         .value(json.getString(PointValue.Fields.value))
                         .build();
    }

    public static class PointValueBuilder {

        public PointValue build() {
            String v = Optional.ofNullable(this.value)
                               .orElse(Optional.ofNullable(rawValue).map(Object::toString).orElse(null));
            Double r = Optional.ofNullable(rawValue)
                               .orElse(Optional.ofNullable(this.value)
                                               .map(x -> Functions.getOrDefault((Double) null, () -> Double.valueOf(x)))
                                               .orElse(null));
            return new PointValue(v, r);
        }

    }

}
