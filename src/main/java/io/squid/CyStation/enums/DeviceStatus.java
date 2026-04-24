package io.squid.CyStation.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceStatus {
    ONLINE("En marche"),
    OFFLINE("A l'arrêt"),
    MAINTENANCE("En maintenance");


    private final String displayValue;

    DeviceStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    @JsonProperty("displayValue")
    public String getDisplayValue() { return displayValue; }

    @JsonProperty("name")
    public String getName() {
        return this.name();
    }
}
