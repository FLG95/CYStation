package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("RADAR")
public class Radar extends Sensor{

    private int detected;

    public int getDetected(){return detected; }
    public void setDetected(int detected){
        this.detected = detected;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.RADAR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.RADAR.getDisplayName();
    }
}
