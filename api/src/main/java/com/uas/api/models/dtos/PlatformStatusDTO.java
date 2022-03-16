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
    /**
     * The total repairs count for the aircraft.
     */
    private final int repairsCount;
    /**
     * The total repairs cost for the aircraft.
     */
    private final BigDecimal repairsCost;
    /**
     * The total parts cost for the aircraft.
     */
    private final BigDecimal partsCost;

    /**
     * Constructor.
     * @param tailNumber the aircraft tail number.
     * @param platformType the platform type.
     * @param platformStatus the platform status.
     * @param flyTimeHours the flight time in hours.
     * @param totalCost the total cost of the aircraft.
     * @param location the location of the platform.
     * @param repairsCount the repairs count of the aircraft.
     * @param repairsCost the repairs cost of the aircraft.
     * @param partsCost the parts cost for the aircraft.
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
        super(tailNumber, platformStatus, platformType, location);
        this.flyTimeHours = flyTimeHours;
        this.totalCost = totalCost;
        this.repairsCount = repairsCount;
        this.repairsCost = repairsCost;
        this.partsCost = partsCost;
    }
}
