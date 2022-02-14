package com.uas.api.models.entities.enums;

public enum PartStatus {
    OPERATIONAL("Operational"),
    AWAITING_REPAIR("Awaiting Repair"),
    BEING_REPAIRED("Being Repaired"),
    BEYOND_REPAIR("Beyond Repair");

    public final String label;

    PartStatus(String label) {
        this.label = label;
    }
}
