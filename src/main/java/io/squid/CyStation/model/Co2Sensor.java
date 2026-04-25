package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CO2_SENSOR")
public class Co2Sensor extends Sensor {

    private int ppmLevel;

    public int getPpmLevel(){return ppmLevel; }
    public void setPpmLevel(int ppmLevel){
        this.ppmLevel = ppmLevel;
    }

    @Override
    public boolean updateTelemetry() {
        this.setPpmLevel(400 + (int)(Math.random() * 400)); // entre 400 et 800 ppm
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.CO2_SENSOR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.CO2_SENSOR.getDisplayName();
    }

}
