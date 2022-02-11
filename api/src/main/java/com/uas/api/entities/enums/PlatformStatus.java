package com.uas.api.entities.enums;

public enum PlatformStatus {
    DESIGN("Design"),
    PRODUCTION("Production"),
    OPERATION("Operation"),
    REPAIR("Repair");

    public final String label;

    PlatformStatus(String label) {
        this.label = label;
    }
}
