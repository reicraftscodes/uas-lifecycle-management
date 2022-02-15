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
@Table(name = "Parts")
public class Part {
    /**
     * Part number id.
     */
    @Id
    @Column(name = "PartNumber")
    private Long partNumber;
    /**
     * Part Type ID.
     */
    @ManyToOne
    @JoinColumn(name = "PartID")
    private PartType partType;
    /**
     * Aircraft.
     */
    @ManyToOne
    @JoinColumn(name = "AircraftTailNumber")
    private Aircraft aircraft;
    /**
     * Location Name.
     */
    @ManyToOne
    @JoinColumn(name = "LocationName")
    private Location location;
    /**
     * Manufacture date.
     */
    @Column(name = "Manufacture")
    private LocalDateTime manufacture;
    /**
     * Part status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PartStatus")
    private PartStatus partStatus;
}
