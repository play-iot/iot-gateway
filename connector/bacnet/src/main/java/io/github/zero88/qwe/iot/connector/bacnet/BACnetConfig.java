package io.github.zero88.qwe.iot.connector.bacnet;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import io.github.zero88.qwe.CarlConfig.AppConfig;
import io.github.zero88.qwe.IConfig;
import io.github.zero88.utils.Strings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class BACnetConfig implements IConfig {

    protected static final int MIN_DEVICE_ID = 80000;
    protected static final int MAX_DEVICE_ID = 90000;

    @Default
    private final int vendorId = 161214;
    @Default
    private final String vendorName = "zero88";
    private int deviceId;
    @Default
    private final String modelName = "QWE-BACnet";
    @Default
    private String deviceName = "Wanna Fly";
    @Default
    private final long maxDiscoverTimeout = 10;
    @Default
    private final TimeUnit maxDiscoverTimeoutUnit = TimeUnit.SECONDS;
    @Default
    private final String completeDiscoverAddress = BACnetConfig.class.getPackage().getName() + ".discover.complete";
    @Default
    private final String readinessAddress = BACnetConfig.class.getPackage().getName() + ".readiness";

    @Override
    public final String key() {
        return "__bacnet__";
    }

    @Override
    public final Class<? extends IConfig> parent() { return AppConfig.class; }

    public int getDeviceId() {
        if (deviceId < 0 || deviceId > ObjectIdentifier.UNINITIALIZED) {
            throw new IllegalArgumentException("Illegal device id: " + deviceId);
        }
        return deviceId = deviceId == 0 ? genDeviceId() : deviceId;
    }

    public String getDeviceName() {
        return deviceName = Strings.isBlank(deviceName) ? modelName + "-" + deviceId : deviceName;
    }

    protected int maxDeviceId() {
        return 80000;
    }

    protected int minDeviceId() {
        return 90000;
    }

    private int genDeviceId() {
        return new SecureRandom().ints(minDeviceId(), maxDeviceId()).findAny().orElse(minDeviceId());
    }

    @JsonIgnore
    public long getMaxTimeoutInMS() {
        return TimeUnit.MILLISECONDS.convert(getMaxDiscoverTimeout(), getMaxDiscoverTimeoutUnit());
    }

}

