package io.github.zero88.qwe.iot.connector.bacnet.mixin;

import io.github.zero88.qwe.dto.JsonData;
import io.github.zero88.utils.Strings;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

public interface BACnetJsonMixin extends JsonData {

    ObjectMapper MAPPER = JsonData.MAPPER.copy().registerModule(BACnetJsonModule.MODULE);

    ObjectMapper LENIENT_MAPPER = MAPPER.copy().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Standardize BACnet key property with lower-case and separate by {@code -}
     *
     * @param keyProp Given key Property
     * @return standard key
     * @see <a href="https://csimn.com/MHelp-SPX-B/spxb-section-14.html">Object Properties</a>
     */
    //    TODO Not sure it is standard
    static String standardizeKey(@NonNull String keyProp) {
        return Strings.transform(keyProp, false, "-");
    }

    @Override
    default ObjectMapper getMapper() {
        return MAPPER;
    }

}
