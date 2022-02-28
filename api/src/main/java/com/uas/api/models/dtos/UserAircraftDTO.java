package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserAircraftDTO {

    private final String tailNumber;
    private final String location;
    private final String platformStatus;
    private final String platformType;
    private final long userAircraftFlyingHours;
    private final long totalAircraftFlyingHours;
}
