package io.squid.CyStation.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CO2_SENSOR")
public class Co2Sensor extends Sensor {

    private int ppmLevel;

    public int getPpmLevel(){return ppmLevel; }
    public void setPpmLevel(int ppmLevel){
        this.ppmLevel = ppmLevel;
    }

}
