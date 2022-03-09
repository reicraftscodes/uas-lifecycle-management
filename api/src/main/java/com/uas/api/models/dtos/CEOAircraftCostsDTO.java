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
public class CEOAircraftCostsDTO {
    /**
     * Stores the total spent on repairs for all aircraft.
     */
    private double totalSpentOnRepairs;
    /**
     * Stores the total spent on parts for all aircraft.
     */
    private double totalSpentOnParts;
    /**
     * Stores the total spent on all aircraft.
     */
    private double totalSpent;
    /**
     *  Stores a list of DTOs which contains aircraft and their costs parts and repairs.
     */
    private List<CEOAircraftDTO> aircraft;




}
