package io.github.zero88.qwe.iot.data.unit;

import io.github.zero88.utils.Reflections.ReflectionField;
import io.github.zero88.utils.Strings;

import lombok.NonNull;

public interface DataTypeCategory extends DataType {

    <T extends DataType, V extends DataType> T convert(V v);

    @Override
    default @NonNull String type() {
        return ReflectionField.constantByName(this.getClass(), "TYPE");
    }

    interface Base extends DataTypeCategory {

        String TYPE = "ALL";
        DataType NUMBER = new NumberDataType();
        DataType PERCENTAGE = new NumberDataType("percentage", "%");
        DataType BOOLEAN = new BooleanDataType();

    }


    interface Power extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(Power.class.getSimpleName());
        DataType KWH = new NumberDataType("kilowatt_hour", "kWh", TYPE);
        DataType DBM = new NumberDataType("dBm", "dBm", TYPE);

    }


    interface Pressure extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(Pressure.class.getSimpleName());
        DataType HPA = new NumberDataType("hectopascal", "hPa", TYPE);

    }


    interface Temperature extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(Temperature.class.getSimpleName());
        DataType FAHRENHEIT = new NumberDataType("fahrenheit", "°F", TYPE);
        DataType CELSIUS = new NumberDataType("celsius", "°C", TYPE);

    }


    interface Velocity extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(Velocity.class.getSimpleName());
        DataType M_PER_SECOND = new NumberDataType("meters_per_second", "m/s", TYPE);
        DataType KM_PER_HOUR = new NumberDataType("kilometers_per_hour", "km/h", TYPE);
        DataType MILE_PER_HOUR = new NumberDataType("miles_per_hour", "mph", TYPE);

    }


    interface AngularVelocity extends Velocity {

        String TYPE = Strings.toSnakeCaseUC(AngularVelocity.class.getSimpleName());
        DataType RPM = new NumberDataType("revolutions_per_minute", "rpm", TYPE);
        DataType RAD_PER_SECOND = new NumberDataType("radians_per_second", "rad/s", TYPE);

    }


    interface Illumination extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(Illumination.class.getSimpleName());
        DataType LUX = new NumberDataType("lux", "lx", TYPE);

    }


    interface ElectricPotential extends DataTypeCategory {

        String TYPE = Strings.toSnakeCaseUC(ElectricPotential.class.getSimpleName());
        DataType VOLTAGE = new NumberDataType("volt", "V", TYPE);

    }

}
