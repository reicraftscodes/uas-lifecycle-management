package com.uas.api.models.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AircraftUserKey implements Serializable {

    @Column(name = "UserID")
    private Long userID;

    @Column(name = "TailNumber")
    private String tailNumber;
}
