package io.github.zero88.qwe.iot.data.entity;

import lombok.NonNull;

/**
 * @param <T> Type of IoT object type
 */
public interface HasObjectType<T> {

    @NonNull T type();

}
