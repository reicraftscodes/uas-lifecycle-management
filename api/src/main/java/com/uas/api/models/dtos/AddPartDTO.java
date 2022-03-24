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
     * The aircraft tail number.
     */
    private final String tailNumber;
    /**
     * The location name.
     */
    private final String locationName;
    /**
     * The manufacture date as a string.
     */
    private final String manufacture;
    /**
     * The status of the part as a string.
     */
    private final String partStatus;
}
