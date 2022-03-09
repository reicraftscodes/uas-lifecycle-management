package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CEOPartRepairDTO {
    /**
     * The id of the repair.
     */
    private long repairID;
    /**
     * The part Type that was repaired.
     */
    private String partType;
    /**
     * The cost of the repair.
     */
    private double cost;

}
