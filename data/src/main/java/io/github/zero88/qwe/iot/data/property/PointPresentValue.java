package io.github.zero88.qwe.iot.data.property;

import java.util.Optional;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.iot.data.IoTProperty;
import io.github.zero88.qwe.iot.data.TimeseriesData;
import io.reactivex.annotations.Nullable;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents for Point value.
 *
 * @since 1.0.0
 */
@Getter
@Jacksonized
@FieldNameConstants
@Builder(builderClassName = "Builder")
public final class PointPresentValue implements JsonData, IoTProperty, TimeseriesData {

    private final int priority;
    @JsonUnwrapped
    private final PointValue pointValue;

    public static PointPresentValue def() {
        return PointPresentValue.builder().priority(PointPriorityArray.DEFAULT_PRIORITY).build();
    }

    @Nullable
    public static PointPresentValue from(@NonNull JsonObject data) {
        if (data.containsKey(Fields.priority) || data.containsKey(PointValue.Fields.rawValue) ||
            data.containsKey(PointValue.Fields.value)) {
            return PointPresentValue.builder()
                                    .priority(data.getInteger(Fields.priority))
                                    .pointValue(PointValue.from(data))
                                    .build();
        }
        return null;
    }

    public static class Builder {

        @Setter
        @Accessors(fluent = true)
        private Integer priority;
        @Setter
        @Accessors(fluent = true)
        private String value;
        @Setter
        @Accessors(fluent = true)
        private Double rawValue;

        public PointPresentValue build() {
            int p = PointPriorityArray.validateAndGet(priority);
            PointValue pv = Optional.ofNullable(pointValue)
                                    .orElseGet(() -> PointValue.builder().value(value).rawValue(rawValue).build());
            return new PointPresentValue(p, pv);
        }

    }

}
