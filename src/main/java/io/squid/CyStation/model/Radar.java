package io.squid.CyStation.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RADAR")
public class Radar extends Sensor{

    private int detected;

    public int getDetected(){return detected; }
    public void setDetected(int detected){
        this.detected = detected;
    }

}
