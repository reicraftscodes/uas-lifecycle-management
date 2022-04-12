package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AircraftAddNewDTO {
    /**
     * The platform status.
     */
    private final String platformStatus;
    /**
     * The location of the platform.
     */
    private final String location;
    /**
     * The type of the platform.
     */
    private final String platformType;
    /**
     * The tail number of the platform.
     */
    private final String tailNumber;

    /**
     * Gets the platform status of a platform.
     * @return the platform status.
     */
    public String getPlatformStatus() {
        return platformStatus;
    }

    /**
     * Gets the location of a platform.
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the platform type.
     * @return the platform type.
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * Gets the tail number.
     * @return the tail number.
     */
    public String getTailNumber() {
        return tailNumber;
    }
}
