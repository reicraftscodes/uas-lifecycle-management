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
public class CEOAircraftPartDTO {
    /**
     * The partName of a specific part.
     */
    private String partName;
    /**
     * The cost of a part.
     */
    private double partCost;
    /**
     * The status of a specific part.
     */
    private String partStatus;
    /**
     * A list of DTOs for any repairs that have taken place on a part.
     */
    private List<CEOPartRepairDTO> repairs;
}
