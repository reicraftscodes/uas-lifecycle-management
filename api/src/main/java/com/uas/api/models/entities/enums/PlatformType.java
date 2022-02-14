package com.uas.api.models.entities.enums;

public enum PlatformType {
    PLATFORM_A("Platform A"),
    PLATFORM_B("Platform B"),
    BOTH("Both");

    public final String name;

    PlatformType(String name) {
        this.name = name;
    }
}
