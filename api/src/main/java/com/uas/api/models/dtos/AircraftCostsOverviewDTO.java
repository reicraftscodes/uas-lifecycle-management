package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *    This DTO is used for returning aircraft costs without specific parts and repairs
 *     for the android version to allow for a quicker response time.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AircraftCostsOverviewDTO {


    /**
     * The tailnumber of a specific aircraft.
     */
    private String tailNumber;
    /**
     * The total spent on repairs for a specific aircraft.
     */
    private double repairCost;
    /**
     * The total spent on parts for a specific aircraft.
     */
    private double partCost;
    /**
     * The total spent on a specific aircraft.
     */
    private double totalCost;

}
