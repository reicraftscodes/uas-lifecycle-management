package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class PartRepairsDTO {
    /**
     * The part number.
     */
    private long partNumber;
    /**
     * The part type name.
     */
    private String partName;
    /**
     * The part repair count.
     */
    private long repairsCount;
    /**
     * The total cost of all repairs for the part.
     */
    private BigDecimal totalRepairsCost;

}
