package com.uas.api.models.entities.enums;

public enum PlatformStatus {
    DESIGN("Design"),
    PRODUCTION("Production"),
    OPERATION("Operational"),
    REPAIR("Repair");

    private final String label;

    PlatformStatus(final String label) {
        this.label = label;
    }

    /**
     * Getter for label.
     * @return the label.
     */
    public String getLabel() {
        return label;
    }
}
