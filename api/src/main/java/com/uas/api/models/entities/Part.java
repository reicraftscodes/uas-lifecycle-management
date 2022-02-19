package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Parts")
public class Part {
    /**
     * Part number id.
     */
    @Id
    @Column(name = "PartNumber")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "PartStatus")
    private PartStatus partStatus;

    public Part(PartType partType, Aircraft aircraft, Location location, PartStatus partStatus){
        this.partType = partType;
        this.aircraft = aircraft;
        this.location = location;
        LocalDateTime dateTime = LocalDateTime.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = dateTime;
        this.partStatus = partStatus;
    }
}
