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
     *
     */
    private double totalSpentOnRepairs;
    /**
     *
     */
    private double totalSpentOnParts;
    /**
     *
     */
    private double totalSpent;
    /**
     * 
     */
    private List<CEOAircraftDTO> aircraft;




}
