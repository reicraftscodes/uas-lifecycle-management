package com.uas.api.models.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AircraftUserKey implements Serializable {

    /**
     * The user id.
     */
    @Column(name = "UserID")
    private Long userID;

    /**
     * The tail number of the aircraft.
     */
    @Column(name = "TailNumber")
    private String tailNumber;
}
