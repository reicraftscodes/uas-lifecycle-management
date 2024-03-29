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
    @Column(name = "tailnumber", unique = true)
    private String tailNumber;
    /**
     * Location.
     */
    @ManyToOne
    @JoinColumn(name = "locationname", referencedColumnName = "locationname")
    private Location location;
    /**
     * Status of platform.
     */
    @Column(name = "platformstatus")
    private PlatformStatus platformStatus;
    /**
     * Type of platform.
     */
    @Column(name = "platformtype")
    private PlatformType platformType;
    /**
     * The total flight time in hours.
     */
    @Column(name = "flyinghours")
    private Integer flyTimeHours;

}
