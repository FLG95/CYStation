package io.squid.CyStation.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequestStatus {
    PENDING("En attente"),
    ACCEPTED("Accepté"),
    REJECTED("Refusé");


    private final String displayValue;

    RequestStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    @JsonProperty("displayValue")
    public String getDisplayValue() { return displayValue; }

    @JsonProperty("name")
    public String getName() {
        return this.name();
    }
}
