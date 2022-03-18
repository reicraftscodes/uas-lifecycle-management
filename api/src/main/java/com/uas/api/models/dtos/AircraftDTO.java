package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AircraftDTO {
    /**
     * Aircraft tail number.
     */
    private String tailNumber;
    /**
     * Location name.
     */
    private String location;
    /**
     * Status of platform.
     */
    private String platformStatus;
    /**
     * Type of platform.
     */
    private String platformType;
    /**
     * The total flight time in hours.
     */
    private Integer flyTimeHours;
}
