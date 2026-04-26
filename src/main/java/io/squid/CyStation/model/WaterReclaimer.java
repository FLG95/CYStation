package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("WATER_RECLAIMER")
public class WaterReclaimer extends Device{

    private double purityLevel;
    private int fillLevel;

    public double getPurityLevel() { return purityLevel; }
    public void setPurityLevel(double purityLevel) { this.purityLevel = purityLevel; }

    public int getFillLevel() { return fillLevel; }
    public void setFillLevel(int fillLevel) { this.fillLevel = fillLevel; }

    @Override
    public boolean updateTelemetry() {
        // Simule une pureté qui fluctue légèrement entre 95% et 100%
        this.setPurityLevel(95.0 + (Math.random() * 5.0));
        // Simule un niveau de cuve aléatoire entre 0 et 1000 litres
        this.setFillLevel((int) (Math.random() * 1000));
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.WATER_RECLAIMER.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.WATER_RECLAIMER.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://watercontrolinc.com/wp-content/uploads/2024/04/Commercial-Water-Softeners-1.png";
    }

    @Override
    public String getTelemetryDisplay() {
        return String.format("Pureté: %.2f%% | Stock: %d L", this.purityLevel, this.fillLevel);
    }

}
