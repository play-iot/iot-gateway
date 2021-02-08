package io.github.zero88.qwe.iot.service.bacnet.service.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.qwe.dto.ErrorMessage;
import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.qwe.dto.msg.RequestData;
import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventMessage;
import io.github.zero88.qwe.event.EventbusClient;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorChannel;
import io.github.zero88.qwe.iot.connector.coordinator.CoordinatorChannel.Fields;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

public class CoordinatorHelper {

    public Single<JsonObject> restartBACnetCovCoordinator(@NonNull SharedDataLocalProxy sharedData) {
        final EventbusClient eventbus = EventbusClient.create(sharedData);
        final CovCoordinatorPersistence persistence = new CovCoordinatorPersistence(sharedData);
        return persistence.list()
                          .flatMapSingle(channel -> registerBACnetCov(eventbus, channel))
                          .collect(RegisterOutput::report, RegisterOutput::merge)
                          .map(JsonData::toJson);
    }

    private Single<RegisterOutput> registerBACnetCov(@NonNull EventbusClient eventbus,
                                                     @NonNull CoordinatorChannel channel) {
        final JsonObject payload = new JsonObject().put(Fields.watcherOption, channel.getWatcherOption().toJson())
                                                   .put(Fields.subscribers, channel.getSubscribers())
                                                   .mergeIn(channel.getSubject());
        final EventMessage msg = EventMessage.initial(EventAction.CREATE_OR_UPDATE,
                                                      RequestData.builder().body(payload).build());
        return eventbus.request(BACnetCOVCoordinator.class.getName(), msg).map(output -> {
            if (output.isError()) {
                return RegisterOutput.builder().key(channel.key()).errorMessage(output.getError()).build();
            }
            return RegisterOutput.builder().key(channel.key()).build();
        }).onErrorReturn(t -> RegisterOutput.builder().key(channel.key()).errorMessage(ErrorMessage.parse(t)).build());
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    private static class RegisterOutput implements JsonData {

        private final List<String> successes;
        private final List<JsonObject> errors;

        private final String key;
        private final ErrorMessage errorMessage;

        static RegisterOutput report() {
            return RegisterOutput.builder().successes(new ArrayList<>()).errors(new ArrayList<>()).build();
        }

        boolean isError() {
            return Objects.nonNull(errorMessage);
        }

        void merge(@NonNull RegisterOutput each) {
            if (each.isError()) {
                this.errors.add(new JsonObject().put(each.key, each.errorMessage.toJson()));
            } else {
                this.successes.add(each.getKey());
            }
        }

        @Override
        public JsonObject toJson(ObjectMapper mapper) {
            final int ss = successes.size();
            final int total = ss + errors.size();
            return new JsonObject().put("message", "Started " + ss + "/" + total + " COV channel")
                                   .mergeIn(JsonData.super.toJson(mapper));
        }

    }

}
