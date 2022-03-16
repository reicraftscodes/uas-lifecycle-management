package com.uas.api.models.dtos;

import com.uas.api.models.dtos.commondtointerfaces.PlatformStatusInterface;
import com.uas.api.models.entities.enums.PlatformStatus;

public class PlatformStatusAndroidDTO extends PlatformStatusInterface {
    /**
     * The location of the aircraft.
     */
    private final String location;

    /**
     * Constructor, which sends back to super.
     * @param tailNumber
     * @param platformStatus
     * @param location
     */
    public PlatformStatusAndroidDTO(final String tailNumber, final PlatformStatus platformStatus, final String location) {
        super(tailNumber, platformStatus);
        this.location = location;
    }

    /**
     * Gets the location for the aircraft.
     * @return the location as a string.
     */
    public String getLocation() {
        return location;
    }
}
