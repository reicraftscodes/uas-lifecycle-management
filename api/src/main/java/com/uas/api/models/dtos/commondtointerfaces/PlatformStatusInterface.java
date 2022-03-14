package com.uas.api.models.dtos.commondtointerfaces;

import com.uas.api.models.entities.enums.PlatformStatus;

public class PlatformStatusInterface {
    /**
     * The tail number of the aircraft.
     */
    private final String tailNumber;
    /**
     * The status of the aircraft. Operational, Beyond Repair, Awaiting Repair, Being Repaired.
     */
    private final PlatformStatus platformStatus;

    /**
     * Constructor.
     * @param tailNumber
     * @param platformStatus
     */
    public PlatformStatusInterface(final String tailNumber, final PlatformStatus platformStatus) {
        this.tailNumber = tailNumber;
        this.platformStatus = platformStatus;
    }

    /**
     * Gets the tail number.
     * @return the tail number.
     */
    public String getTailNumber() {
        return tailNumber;
    }

    /**
     * Gets the platform status.
     * @return the platform status.
     */
    public PlatformStatus getPlatformStatus() {
        return platformStatus;
    }
}
