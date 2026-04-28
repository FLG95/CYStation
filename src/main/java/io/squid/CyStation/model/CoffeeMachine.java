package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("COFFEE")
public class CoffeeMachine extends Device {

    private int cupsServed;
    private int waterLevel;

    public CoffeeMachine(){
        super(5);
    }

    public int getCupsServed() { return cupsServed; }
    public void setCupsServed(int cupsServed) { this.cupsServed = cupsServed; }

    public int getWaterLevel() { return waterLevel; }
    public void setWaterLevel(int waterLevel) { this.waterLevel = waterLevel; }

    @Override
    public boolean updateTelemetry() {
        this.setCupsServed((int)(Math.random() * 50));       // 0 à 50 cafés servis
        this.setWaterLevel(200 + (int)(Math.random() * 800)); // 200 à 1000 mL
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.COFFEE.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.COFFEE.getDisplayName();
    }

    @Override
    public String getDeviceImage() {
        return "https://creapills.com/wp-content/uploads/2023/07/lg-machine-a-cafe-apollo-11-2-1160x770.jpeg";
    }

    @Override
    public String getTelemetryDisplay() {
        return String.format("Cafés servis : %d | Eau : %d mL", this.cupsServed, this.waterLevel);
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "CUPS_SERVED", (double) this.cupsServed,
                "WATER_LEVEL", (double) this.waterLevel
        );
    }
}
