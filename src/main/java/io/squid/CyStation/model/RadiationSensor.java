package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RADIATION_SENSOR")
public class RadiationSensor extends Sensor{


    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.RADIATION_SENSOR.getDisplayName();
    }
}
