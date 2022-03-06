package com.uas.api.models.entities.enums;

public enum PlatformAvailability {
    ASSIGNED("Assigned"),
    UNASSIGNED("Unassigned");
    private final String label;

    PlatformAvailability(final String label) {
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
