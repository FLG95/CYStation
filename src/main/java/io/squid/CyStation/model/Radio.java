package io.squid.CyStation.model;


import io.squid.CyStation.enums.DeviceCategory;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.Map;

@Entity
@DiscriminatorValue("Radio")
public class Radio extends Device{

    private int contact;

    public int getContact(){return contact; }
    public void setContact(int contact) {
        this.contact = contact;
    }

    @Override
    public boolean updateTelemetry() {
        this.setContact(Math.random() > 0.5 ? 1 : 0); // entre 0 et 1
        return true;
    }

    @Override
    public String getDeviceCategoryCode() {
        return DeviceCategory.RADIO.name();
    }

    @Override
    public String getDeviceDisplayName() {
        return DeviceCategory.RADIO.getDisplayName();
    }

    @Override
    public String getDeviceImage(){
        return "https://cdn.shopify.com/s/files/1/0019/5952/files/CO2-Molecule-Image.jpg";
    }

    @Override
    public String getTelemetryDisplay(){
        return this.contact + "";
    }

    @Transient
    @Override
    public Map<String, Double> getTelemetryMetrics() {
        return Map.of(
                "CONTACT", (double) this.contact
        );
    }

}
