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
    private final long partNumber;
    /**
     * The part type name.
     */
    private final String partName;
    /**
     * The part repair count.
     */
    private final long repairsCount;
    /**
     * The total cost of all repairs for the part.
     */
    private final BigDecimal totalRepairsCost;

}
