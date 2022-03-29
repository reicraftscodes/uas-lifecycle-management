package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class PartDTO {
    /**
     * The part number.
     */
    private final long partNumber;
    /**
     * The part type.
     */
    private final String partType;
    /**
     * The part cost.
     */
    private final BigDecimal cost;
    /**
     * The part weight.
     */
    private final long weight;
    /**
     * The part typical failure hours.
     */
    private final long typicalFailureHours;
    /**
     * A list of locations where the part is stocked.
     */
    private final List<PartStockDTO> stockLocations;
    /**
     * A list of platforms that the part is compatible with.
     */
    private final List<String> compatiblePlatforms;
}
