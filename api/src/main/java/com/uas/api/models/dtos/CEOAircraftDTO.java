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
public class CEOAircraftDTO {
    private String tailNumber;

    private double repairCost;

    private double partCost;

    private double totalCost;

    private List<CEOAircraftPartDTO> parts;
}
