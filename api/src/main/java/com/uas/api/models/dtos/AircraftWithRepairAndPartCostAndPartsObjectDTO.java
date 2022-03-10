package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AircraftWithRepairAndPartCostAndPartsObjectDTO {
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
    /**
     * A List of DTOs which contain part names, costs, statuses, and any repairs on that part.
     */
    private List<AircraftPartCostDTO> parts;
}
