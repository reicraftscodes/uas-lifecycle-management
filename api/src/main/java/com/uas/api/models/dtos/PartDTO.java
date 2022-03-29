package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class PartDTO {

    private final long partNumber;

    private final String partType;

    private final BigDecimal cost;

    private final long weight;

    private final long typicalFailureHours;

    private final List<PartStockDTO> stockLocations;

    private final List<String> compatiblePlatforms;
}
