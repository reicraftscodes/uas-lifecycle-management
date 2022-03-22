package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class AddPartDTO {
    /**
     * EHEH.
     */
    private final Long partType;
    /**
     * EHEH.
     */
    private final String tailNumber;
    /**
     * EHEH.
     */
    private final String locationName;
    /**
     * EHEH.
     */
    private final String manufacture;
    /**
     * EHEH.
     */
    private final String partStatus;
}
