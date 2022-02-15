package com.uas.api.models.entities.enums;

public enum PlatformType {
    PLATFORM_A("Platform A"),
    PLATFORM_B("Platform B"),
    BOTH("Both");

    private final String name;

    PlatformType(final String name) {
        this.name = name;
    }

    /**
     * Getter for name.
     * @return the name.
     */
    public String getName() {
        return name;
    }
}
