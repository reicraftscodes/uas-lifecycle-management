package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PartStockLevelDTO {
    /**
     * Part name.
     */
    private String partName;
    /**
     * Location.
     */
    private String location;
    /**
     * Stock level percentage.
     */
    private double stockLevelPercentage;
}
