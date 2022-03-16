package com.uas.api.models.dtos;

import com.uas.api.models.dtos.commondtointerfaces.PlatformStatusInterface;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;

public class PlatformStatusAndroidDTO extends PlatformStatusInterface {

    /**
     * Constructor, which sends back to super.
     * @param tailNumber
     * @param platformStatus
     * @param location
     * @param platformType
     */
    public PlatformStatusAndroidDTO(final String tailNumber, final PlatformStatus platformStatus, final String location, final PlatformType platformType) {
        super(tailNumber, platformStatus, platformType, location);
    }

}
