package io.github.zero88.qwe.iot.connector;

import io.github.zero88.qwe.dto.JsonData;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents for the thing that is being discovered or watched or supervised
 */
public interface Subject extends JsonData {

    @JsonProperty("key")
    String key();

    default JsonObject toDetail() {
        return JsonData.super.toJson();
    }

}
