package com.uas.api.models.dtos;

import com.uas.api.models.auth.User;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUserKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;


@AllArgsConstructor
@Getter
@Setter
public class AircraftUserDTO {

    /**
     * User.
     */
    private User user;

    /**
     * Aircraft.
     */
    private Aircraft aircraft;

    /**
     * The users total flight time in hours for the aircraft.
     */
    private Long userFlyingHours;

}
