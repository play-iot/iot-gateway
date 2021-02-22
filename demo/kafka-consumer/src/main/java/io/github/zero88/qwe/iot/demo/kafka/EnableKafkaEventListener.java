package io.github.zero88.qwe.iot.demo.kafka;

import java.util.Collection;
import java.util.Collections;

import io.github.zero88.qwe.event.EventAction;
import io.github.zero88.qwe.event.EventContractor;
import io.github.zero88.qwe.event.EventListener;

import lombok.NonNull;

public final class EnableKafkaEventListener implements EventListener {

    @Override
    public @NonNull Collection<EventAction> getAvailableEvents() {
        return Collections.singletonList(EventAction.UPDATE);
    }

    @EventContractor(action = "UPDATE", returnType = String.class)
    public String update() {
        return "success";
    }

}
