package io.github.zero88.qwe.iot.data;

import java.util.Collection;

import io.github.zero88.qwe.dto.JsonData;

public interface IoTEntities<K, T extends IoTEntity<K>> extends JsonData {

    IoTEntities<K, T> add(T ioTEntity);

    Collection<T> entities();

}
