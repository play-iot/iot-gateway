package io.github.zero88.qwe.iot.connector.bacnet.entity;

import io.github.zero88.qwe.iot.connector.bacnet.BACnetProtocol;
import io.github.zero88.qwe.iot.data.IoTEntity;

import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;

public interface BACnetEntity<K> extends BACnetProtocol, IoTEntity<K>, BACnetJsonMixin {

}
