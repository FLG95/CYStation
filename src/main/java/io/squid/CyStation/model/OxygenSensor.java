package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OXYGEN_SENSOR")
public class OxygenSensor extends Sensor{

    private int o2Percentage;

    public int getO2Percentage(){ return o2Percentage; }

    public void setO2Percentage(int o2Percentage){
        this.o2Percentage = o2Percentage;
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.OXYGEN_SENSOR.getDisplayName();
    }
}
