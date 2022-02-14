package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Parts")
public class Part {

    @Id
    @Column(name="PartNumber")
    private Long partNumber;

    @ManyToOne
    @JoinColumn(name = "PartID")
    private PartType partType;

    @ManyToOne
    @JoinColumn(name = "AircraftTailNumber")
    private Aircraft aircraft;

    @ManyToOne
    @JoinColumn(name = "LocationName")
    private Location location;

    @Column(name="Manufacture")
    private LocalDateTime manufacture;

    @Enumerated(EnumType.STRING)
    @Column(name="PartStatus")
    private PartStatus partStatus;
}
