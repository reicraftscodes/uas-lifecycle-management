package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PartStockLevelDTO {

    private String partName;
    private String location;
    private double stockLevelPercentage;
}
