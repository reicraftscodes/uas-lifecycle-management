package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAircraftDTO {

    /**
     * The tail number of the aircraft.
     */
    private final String tailNumber;

    /**
     * The location of the aircraft.
     */
    private final String location;

    /**
     * The platform status.
     */
    private final String platformStatus;

    /**
     * The platform type.
     */
    private final String platformType;

    /**
     * The users total flight time for the aircraft in hours.
     */
    private final long userAircraftFlyingHours;

    /**
     * The aircraft total flight time in hours.
     */
    private final long totalAircraftFlyingHours;
}
