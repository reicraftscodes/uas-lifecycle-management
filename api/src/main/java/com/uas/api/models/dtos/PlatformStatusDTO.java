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
    private final Integer flyTimeHours;
    /**
     * The Aircraft status.
     */
    private final PlatformStatus platformStatus;
    /**
     * The cost of the Aircraft.
     */
    private Integer totalCost;

}
