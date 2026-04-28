package io.squid.CyStation.enums;

import io.squid.CyStation.model.*;

import java.util.function.Supplier;

public enum DeviceCategory {
    GENERATOR("Générateur", Generator::new),
    CO2_SENSOR("Capteur CO2", Co2Sensor::new),
    OXYGEN_SENSOR("Capteur Oxygène", OxygenSensor::new),
    RADIATION_SENSOR("Capteur de Radiation", RadiationSensor::new),
    RADAR("Radar Orbital", Radar::new),
    RADIO("Relais Radio", Radio::new),
    WATER_RECLAIMER("Recycleur d'eau", WaterReclaimer::new),
    COFFEE("Machine à café", CoffeeMachine::new),
    BOOZE("Frigo à bière", BoozeFridge::new);

    private final String displayName;
    private final Supplier<Device> instantiator;

    DeviceCategory(String displayName, Supplier<Device> instantiator) {
        this.displayName = displayName;
        this.instantiator = instantiator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Device createInstance() { // replace switch
        return instantiator.get();
    }
}