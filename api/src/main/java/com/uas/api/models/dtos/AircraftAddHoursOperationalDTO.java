package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AircraftAddHoursOperationalDTO {

    /**
     * The aircraft tailNumber to add hours too.
     */
    private String tailNumber;

    /**
     * The hours to add.
     */
    private Integer hoursToAdd;
}

