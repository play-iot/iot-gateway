package io.github.zero88.qwe.iot.connector.mock;

import io.github.zero88.qwe.iot.connector.Subject;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@Accessors(fluent = true)
@RequiredArgsConstructor
public class MockSubject implements Subject {

    @NonNull
    private final String key;

}
