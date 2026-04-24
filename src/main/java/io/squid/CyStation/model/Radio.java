package io.squid.CyStation.model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Radio")
public class Radio extends Device{

    private int contact;

    public int getContact(){return contact; }
    public void setContact(int contact) {
        this.contact = contact;
    }
}
