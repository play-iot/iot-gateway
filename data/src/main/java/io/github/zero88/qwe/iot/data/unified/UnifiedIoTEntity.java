package io.github.zero88.qwe.iot.data.unified;

import java.util.UUID;

import io.github.zero88.qwe.iot.data.IoTEntity;
import io.github.zero88.qwe.protocol.Protocol;
import io.github.zero88.utils.UUID64;

import lombok.NonNull;

/**
 * Represents for an {@code unified IoT} entity that is used for persistence
 */
public interface UnifiedIoTEntity extends IoTEntity<UUID> {

    /**
     * UUID in Base64 format
     *
     * @return UUID in base64 format
     */
    default @NonNull String keyAsString() {
        return UUID64.uuidToBase64(key());
    }

    default @NonNull Protocol protocol() {
        return Protocol.Unification;
    }

    /**
     * A particular data
     *
     * @return a particular data
     * @see ParticularData
     */
    @NonNull ParticularData particularData();

}
