package com.uas.api.models.dtos;

import com.uas.api.models.dtos.commondtointerfaces.PlatformStatusInterface;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PlatformStatusDTO extends PlatformStatusInterface {
    /**
     * The hours operational.
     */
    private final int flyTimeHours;
    /**
     * The cost of the Aircraft.
     */
    private final BigDecimal totalCost;

    private final String platformType;

    private final String location;

    private final int repairsCount;

    private final BigDecimal repairsCost;

    private final BigDecimal partsCost;

    /**
     * Constructor.
     * @param tailNumber the aircraft tail number.
     * @param platformStatus the platform status.
     * @param flyTimeHours the flight time in hours.
     * @param totalCost
     * @param location
     * @param repairsCount
     * @param repairsCost
     * @param partsCost
     */
    public PlatformStatusDTO(
            final String tailNumber,
            final PlatformType platformType,
            final PlatformStatus platformStatus,
            final int flyTimeHours,
            final BigDecimal totalCost,
            final String location,
            final int repairsCount,
            final BigDecimal repairsCost,
            final BigDecimal partsCost) {
        super(tailNumber, platformStatus);
        this.flyTimeHours = flyTimeHours;
        this.platformType = platformType.getName();
        this.totalCost = totalCost;
        this.location = location;
        this.repairsCount = repairsCount;
        this.repairsCost = repairsCost;
        this.partsCost = partsCost;
    }
}
