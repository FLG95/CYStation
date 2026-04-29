package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import io.squid.CyStation.enums.DeviceStatus;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("GENERATOR")
public class Generator extends Device {

    private int productionValue = 50;

    public Generator(){
        super(0);
    }


    public void setProduction(int productionValue){
        this.productionValue = productionValue;
    }

    @Override
    public boolean updateTelemetry() {
        if (this.getStatus() == DeviceStatus.ONLINE) {
            this.setProduction(80 + (int)(Math.random() * 20));
        } else {
            this.setProduction(0);
        }
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
        return super.getTelemetryMetrics();
    }

    @Override
    public int getProductionValue() {

        if (this.getStatus() == DeviceStatus.ONLINE) {
            return this.productionValue;
        }
        return 0;
    }

}
