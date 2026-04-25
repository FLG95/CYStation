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
    public boolean updateTelemetry() {
        this.setDetected((int)(Math.random() * 6)); // entre 0 et 5
        return true;
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
