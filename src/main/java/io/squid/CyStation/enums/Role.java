package io.squid.CyStation.enums;

public enum Role{
    PASSENGER("Passager"),
    SCIENTIST("Scientifique"),
    ADMIN("Administrateur");


    private final String displayValue;

    Role(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() { return displayValue; }

}

