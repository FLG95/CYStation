package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;


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

    @Override
    public String getDeviceImage(){
        return "https://europeanspaceflight.com/wp-content/uploads/2026/01/CNES-Awards-Loft-Orbital-Contract-for-French-Radar-Imaging-Demo.webp";
    }

    @Override
    public String getTelemetryDisplay(){
        return this.detected + "";
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "DETECTED", (double) this.detected
        );
    }

}
