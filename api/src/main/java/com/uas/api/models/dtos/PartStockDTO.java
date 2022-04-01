package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PartStockDTO {
    /**
     * The part number.
     */
    private final long partNumber;
    /**
     * The location name of where the part is stocked.
     */
    private final String location;
    /**
     * The part stock count at the location.
     */
    private final long stockCount;
}
