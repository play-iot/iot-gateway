package io.github.zero88.qwe.iot.service.bacnet.service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.zero88.qwe.component.SharedDataLocalProxy;
import io.github.zero88.utils.Reflections.ReflectionClass;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BACnetApisHelper {

    public static Set<? extends BACnetApis> createServices(@NonNull SharedDataLocalProxy sharedData) {
        final Map<Class, Object> inputs = Collections.singletonMap(SharedDataLocalProxy.class, sharedData);
        return ReflectionClass.stream(BACnetApisHelper.class.getPackage().getName(), BACnetApis.class,
                                      ReflectionClass.publicClass())
                              .map(clazz -> ReflectionClass.createObject(clazz, inputs))
                              .filter(Objects::nonNull)
                              .collect(Collectors.toSet());
    }

}
