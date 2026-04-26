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
    public boolean updateTelemetry() {
        this.setO2Percentage(0 + (int)(Math.random() * 101)); // entre 0 et 100
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.OXYGEN_SENSOR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.OXYGEN_SENSOR.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://cdn.shopify.com/s/files/1/0019/5952/files/CO2-Molecule-Image.jpg";
    }

    @Override
    public String getTelemetryDisplay(){
        return this.o2Percentage + "%";
    }

}

