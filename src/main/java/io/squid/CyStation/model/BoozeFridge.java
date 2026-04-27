package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("BoozeFridge")
public class BoozeFridge extends Device{

    private double purityLevel;
    private int fillLevel;


    public BoozeFridge(){
        super(10);
    }

    public double getPurityLevel() { return purityLevel; }
    public void setPurityLevel(double purityLevel) { this.purityLevel = purityLevel; }

    public int getFillLevel() { return fillLevel; }
    public void setFillLevel(int fillLevel) { this.fillLevel = fillLevel; }

    @Override
    public boolean updateTelemetry() {
        this.setPurityLevel(95.0 + (Math.random() * 5.0));
        this.setFillLevel((int) (Math.random() * 1000));
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.BOOZE.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.BOOZE.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://i.f1g.fr/media/ext/704x396_cropupscale/avis-vin.lefigaro.fr/var/src/203/bieres-6454fa1c133c4194069230.jpg";
    }

    @Override
    public String getTelemetryDisplay() {
        return String.format("Pureté: %.2f%% | Stock: %d L", this.purityLevel, this.fillLevel);
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "PURITY_LEVEL", (double) this.purityLevel,
                "FILL_OUTPUT", (double) this.fillLevel
        );
    }

}
