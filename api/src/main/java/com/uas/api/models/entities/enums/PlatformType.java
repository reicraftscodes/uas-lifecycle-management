package com.uas.api.models.entities.enums;

public enum PlatformType {
    PLATFORM_A("Platform_A"),
    PLATFORM_B("Platform_B"),
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
