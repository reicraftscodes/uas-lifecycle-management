package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
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
@Table(name = "Aircraft")
public class Aircraft {
    /**
     * Aircraft ID.
     */
    @Id
    @Column(name = "TailNumber", unique = true)
    private String tailNumber;
    /**
     * Location.
     */
    @ManyToOne
    @JoinColumn(name = "LocationName")
    private Location location;
    /**
     * Status of platform.
     */
    @Column(name = "PlatformStatus")
    private PlatformStatus platformStatus;
    /**
     * Type of platform.
     */
    @Column(name = "PlatformType")
    private PlatformType platformType;
    /**
     * The total flight time in hours.
     */
    @Column(name = "FlyingHours")
    private Integer flyTimeHours;

    /**
     * Number of hours operational.
     */
    @Column(name = "HoursOperational")
    private Integer hoursOperational;

    public Aircraft(String tailNumber, Location location, PlatformStatus platformStatus, PlatformType platformType, Integer flyTimeHours) {
        this.tailNumber = tailNumber;
        this.location = location;
        this.platformStatus = platformStatus;
        this.platformType = platformType;
        this.flyTimeHours = flyTimeHours;
    }
}
