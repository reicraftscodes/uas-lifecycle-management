package com.uas.api.models.dtos.commondtointerfaces;

import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import lombok.Getter;

@Getter
public class PlatformStatusInterface {
    /**
     * The tail number of the aircraft.
     */
    private final String tailNumber;
    /**
     * The status of the aircraft. Operational, Beyond Repair, Awaiting Repair, Being Repaired.
     */
    private final String platformStatus;
    /**
     * The platform type.
     */
    private final String platformType;
    /**
     * The location name.
     */
    private final String location;

    /**
     * Constructor.
     * @param tailNumber
     * @param platformStatus
     * @param platformType
     * @param location
     */
    public PlatformStatusInterface(final String tailNumber, final PlatformStatus platformStatus, final PlatformType platformType, final String location) {
        this.tailNumber = tailNumber;
        this.platformStatus = platformStatus.getLabel();
        this.platformType = platformType.getName();
        this.location = location;
    }
}
