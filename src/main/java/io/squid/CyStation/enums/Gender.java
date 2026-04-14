package io.squid.CyStation.enums;

public enum Gender {
    MAN("Homme"),
    WOMAN("Femme"),
    OTHER("Autres");

    private final String displayValue;

    Gender(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() { return displayValue; }
}
