package com.uas.api.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "repairs")
public class Repair {

    /**
     * Repair id.
     */
    @Id
    @Column(name = "RepairID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Part number.
     */
    @ManyToOne
    @JoinColumn(name = "aircraftPartID", referencedColumnName = "AircraftPartID")
    private AircraftPart aircraftPart;

    /**
     * Cost of the repair.
     */
    @Column(name = "cost")
    private BigDecimal cost;

}
