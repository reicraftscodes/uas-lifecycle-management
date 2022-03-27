package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "AircraftPart")
public class AircraftPart {

    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AircraftPartID")
    private Long id;

    /**
     * Aircraft Tail Number.
     */
    @ManyToOne
    @JoinColumn(name = "AircraftTailNumber", referencedColumnName = "TailNumber")
    private Aircraft aircraft;

    /**
     * partid.
     */
    @ManyToOne
    @JoinColumn(name = "partid", referencedColumnName = "partid")
    private Part part;

    /**
     * PartStatus.
     */
    @Column(name = "PartStatus")
    private PartStatus partStatus;

    /**
     * FlightHours.
     */
    @Column(name = "FlightHours")
    private Integer FlightHours;

    public AircraftPart(Aircraft aircraft, Part part, PartStatus partStatus, Integer flightHours) {
        this.aircraft = aircraft;
        this.part = part;
        this.partStatus = partStatus;
        FlightHours = flightHours;
    }
}
