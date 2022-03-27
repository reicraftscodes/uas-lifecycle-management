package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class AddPartDTO {
    /**
     * The partType id.
     */
    private final Long partType;
    /**
     * The part name
     */
    private final String partName;
    /**
     * The location name.
     */
    private final String locationName;
    /**
     * The manufacture date as a string.
     */
    private final String manufacture;
    private final double price;
    private final long weight;
}
