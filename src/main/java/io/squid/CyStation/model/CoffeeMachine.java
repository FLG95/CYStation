package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CoffeeMachine")
public class CoffeeMachine extends Device{

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
        return DeviceCategory.CAFE.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.CAFE.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://creapills.com/wp-content/uploads/2023/07/lg-machine-a-cafe-apollo-11-2-1160x770.jpeg";
    }

    @Override
    public String getTelemetryDisplay() {
        return String.format("Pureté: %.2f%% | Stock: %d L", this.purityLevel, this.fillLevel);
    }

}
