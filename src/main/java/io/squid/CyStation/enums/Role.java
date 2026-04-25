package io.squid.CyStation.enums;

public enum Role{
    ROLE_PASSENGER("Passager"),
    ROLE_SCIENTIST("Scientifique"),
    ROLE_ADMIN("Administrateur");


    private final String displayValue;

    Role(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() { return displayValue; }

}

