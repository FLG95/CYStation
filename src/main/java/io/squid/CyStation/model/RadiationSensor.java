package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("RADIATION_SENSOR")
public class RadiationSensor extends Sensor{

    private int mSvLevel;
    public int getmSvLevel(){return mSvLevel; }
    public void setmSvLevel(int mSvLevel){
        this.mSvLevel = mSvLevel;
    }

    @Override
    public boolean updateTelemetry() {
        this.setmSvLevel(40 + (int)(Math.random() * 41));  // entre 40 et 80
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.RADIATION_SENSOR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.RADIATION_SENSOR.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://cdn.shopify.com/s/files/1/0019/5952/files/CO2-Molecule-Image.jpg";
    }

    @Override
    public String getTelemetryDisplay(){
        return this.mSvLevel + "mSv";
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "MSV_LEVEL", (double) this.mSvLevel
        );
    }

}