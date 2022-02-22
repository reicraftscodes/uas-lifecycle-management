package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class PartRepairsDTO {
    private long partNumber;
    private String partName;
    private long repairsCount;
    private BigDecimal totalRepairsCost;

}
