package io.github.zero88.qwe.iot.service.bacnet.service;

import io.github.zero88.qwe.iot.connector.ConnectorServiceApis;
import io.github.zero88.utils.Urls;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zero88.qwe.iot.connector.bacnet.BACnetProtocol;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryLevel;
import io.github.zero88.qwe.iot.connector.bacnet.discovery.DiscoveryParams;
import io.github.zero88.qwe.iot.connector.bacnet.mixin.BACnetJsonMixin;

import lombok.NonNull;

/**
 * Represents {@code BACnet public API} services
 */
public interface BACnetApis extends BACnetProtocol, ConnectorServiceApis {

    @Override
    default ObjectMapper mapper() {
        return BACnetJsonMixin.MAPPER;
    }

    @Override
    default @NonNull String servicePath() {
        return DiscoveryParams.genServicePath(level());
    }

    @Override
    default String paramPath() {
        return Urls.toCapture(DiscoveryParams.genParamPath(level()));
    }

    @NonNull DiscoveryLevel level();

}
