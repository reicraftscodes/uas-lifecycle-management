package com.uas.api.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlatformStatusDTO {

    /**
     * The Aircraft tail number.
     */
    private final String tailNumber;
    /**
     * The hours operational.
     */
    private final Integer hoursOperational;
    /**
     * The Aircraft status.
     */
    private final String platformStatus;
    /**
     * The availability of the Aircraft.
     */
    private final String platformAvailability;

}
