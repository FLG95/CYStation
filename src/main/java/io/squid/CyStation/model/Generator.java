package io.squid.CyStation.model;

import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GENERATOR")
public class Generator extends Device {

    private int productionValue;

    public int getProductionValue(){return productionValue; }
    public void setProductionValue(int productionValue){
        this.productionValue = productionValue;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.GENERATOR.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.GENERATOR.getDisplayName();
    }

}
