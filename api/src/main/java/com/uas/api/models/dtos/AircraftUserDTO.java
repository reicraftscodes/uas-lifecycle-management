package com.uas.api.models.dtos;

import com.uas.api.models.auth.User;
import com.uas.api.models.entities.Aircraft;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AircraftUserDTO {

    /**
     * The user assigned to the aircraft.
     */
    private User user;

    /**
     * The Aircraft the user is assigned to.
     */
    private Aircraft aircraft;

    /**
     * The users total flight time in hours for the aircraft.
     */
    private Long userFlyingHours;

}
