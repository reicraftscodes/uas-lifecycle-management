package com.uas.api.models.entities.enums;

public enum PartStatus {
    OPERATIONAL("Operational"),
    AWAITING_REPAIR("Awaiting Repair"),
    BEING_REPAIRED("Being Repaired"),
    BEYOND_REPAIR("Beyond Repair");

    private final String label;

    PartStatus(final String label) {
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
