package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AircraftUserKeyDTO {

    /**
     * The id of the user to be assigned to the aircraft.
     */
    private Long userID;

    /**
     * The aircraft tailNumber to assign a user to.
     */
    private String tailNumber;

}
