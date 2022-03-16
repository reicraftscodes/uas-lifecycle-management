package com.uas.api.models.dtos;

import com.uas.api.models.dtos.commondtointerfaces.PlatformStatusInterface;
import com.uas.api.models.entities.enums.PlatformStatus;
import lombok.Getter;

@Getter
public class PlatformStatusDTO extends PlatformStatusInterface {
    /**
     * The hours operational.
     */
    private final Integer flyTimeHours;
    /**
     * The cost of the Aircraft.
     */
    private final Integer totalCost;

    /**
     * Constructor.
     * @param tailNumber
     * @param flyTimeHours
     * @param platformStatus
     * @param totalCost
     */
    public PlatformStatusDTO(final String tailNumber, final Integer flyTimeHours, final PlatformStatus platformStatus,  final Integer totalCost) {
        super(tailNumber, platformStatus);
        this.flyTimeHours = flyTimeHours;
        this.totalCost = totalCost;
    }
}
