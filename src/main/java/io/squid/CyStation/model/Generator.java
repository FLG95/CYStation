package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("GENERATOR")
public class Generator extends Device {

    private int productionValue;

    public int getProductionValue(){return productionValue; }
    public void setProductionValue(int productionValue){
        this.productionValue = productionValue;
    }

    @Override
    public boolean updateTelemetry() {
        this.setProductionValue(80 + (int)(Math.random() * 20)); // entre 80 et 100
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.GENERATOR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.GENERATOR.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://www.ringpower.com/media/5ayhz1gv/generator_exterior_23.png?entropycrop=128&width=1000";
    }

    @Override
    public String getTelemetryDisplay(){
        return this.productionValue + "GW";
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "PRODUCTION_VALUE", (double) this.productionValue
        );
    }

}
