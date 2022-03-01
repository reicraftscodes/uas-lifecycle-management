package com.uas.api.models.entities;

import com.uas.api.models.auth.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Aircraft_User")
public class AircraftUser {

    /**
     * The AircraftUser id.
     */
    @EmbeddedId
    private AircraftUserKey id;

    /**
     * User.
     */
    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    private User user;

    /**
     * Aircraft.
     */
    @ManyToOne
    @MapsId("tailNumber")
    @JoinColumn(name = "TailNumber")
    private Aircraft aircraft;

    /**
     * The users total flight time in hours for the aircraft.
     */
    @Column(name = "FlyingHours")
    private Long userFlyingHours;
}
