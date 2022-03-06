package com.uas.api.models.dtos;
import com.uas.api.models.entities.enums.PlatformAvailability;
import com.uas.api.models.entities.enums.PlatformStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlatformStatusDTO {

    /**
     * The Aircraft tail number.
     */
    private final String tailNumber;
    /**
     * The hours operational.
     */
    private final Integer hoursOperational;
    /**
     * The Aircraft status.
     */
    private final PlatformStatus platformStatus;
    /**
     * The availability of the Aircraft.
     */
    private PlatformAvailability platformAvailability;
    /**
     * The total cost of the Aircraft.
     */
    private Integer totalCost;

    public PlatformStatusDTO(String tailNumber, Integer hoursOperational, PlatformStatus platformStatus) {
        this.tailNumber = tailNumber;
        this.hoursOperational = hoursOperational;
        this.platformStatus = platformStatus;
    }
}
