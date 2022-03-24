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
    @Column(name = "AircraftTailNumber")
    private Aircraft aircraft;

    /**
     * PartNumber.
     */
    @OneToOne
    @Column(name = "PartNumber")
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

}
