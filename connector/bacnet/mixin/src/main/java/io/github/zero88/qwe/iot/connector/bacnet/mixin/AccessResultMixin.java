package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import lombok.NonNull;

public class AccessResultMixin implements EncodableMixin<SequenceOf<Result>> {

    private final Map<PropertyIdentifier, Choice> map = new HashMap<>();

    public static AccessResultMixin create(@NonNull SequenceOf<Result> results) {
        return new AccessResultMixin().addAll(results.getValues());
    }

    public static AccessResultMixin create(@NonNull List<Result> results) {
        return new AccessResultMixin().addAll(results);
    }

    public static AccessResultMixin create(Result... results) {
        return new AccessResultMixin().addAll(Arrays.asList(results));
    }

    private void add(Result result) {
        if (Objects.isNull(result)) {
            return;
        }
        this.map.put(result.getPropertyIdentifier(), result.getReadResult());
    }

    public AccessResultMixin addAll(@NonNull List<Result> results) {
        results.forEach(this::add);
        return this;
    }

    @Override
    public SequenceOf<Result> unwrap() {
        return new SequenceOf<>(this.map.entrySet()
                                        .stream()
                                        .map(entry -> new Result(entry.getKey(), null, entry.getValue().getDatum()))
                                        .collect(Collectors.toList()));
    }

    @Override
    public JsonObject toJson(@NonNull ObjectMapper mapper) {
        return mapper.convertValue(this.map, JsonObject.class);
    }

}
