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
    @JoinColumn(name = "tailNumber", referencedColumnName = "TailNumber")
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

    private Double flightHours;
    /**
     *  Constructor for AircraftPart.
     *  @param aircraft The aircraft associated with the part.
     *  @param part The part to be associated with the aircraft.
     *  @param partStatus The partStatus of the aircraft part.
     *  @param flightHours The amount of hours the aircraft has flown.
     */
    public AircraftPart(final Aircraft aircraft, final Part part, final PartStatus partStatus, final Double flightHours) {
        this.aircraft = aircraft;
        this.part = part;
        this.partStatus = partStatus;
        this.flightHours = flightHours;
    }
}
