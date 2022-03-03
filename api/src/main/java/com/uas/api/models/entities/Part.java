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
    /**
     * Time the part has spent flying.
     */
    @Column(name = "FlyTimeHours")
    private Integer flyTimeHours;

    /**
     *  Constructor for a part.
     * @param partType Type of part.
     * @param aircraft Aircraft the part is associated with.
     * @param location Location of the part.
     * @param partStatus Status of the part.
     */
    public Part(final PartType partType, final Aircraft aircraft, final Location location, final PartStatus partStatus) {
        this.partType = partType;
        this.aircraft = aircraft;
        this.location = location;
        this.manufacture = LocalDateTime.now();
        this.partStatus = partStatus;
    }

    /**
     * Constructor for a part.
     * @param partType Type of part.
     * @param aircraft Aircraft the part is associated with.
     * @param location Location of the part.
     * @param manufacture Date the part was manufactured.
     * @param partStatus Status of the part.
     */
    public Part(final PartType partType, final Aircraft aircraft, final Location location, final String manufacture, final PartStatus partStatus) {
        this.partType = partType;
        this.aircraft = aircraft;
        this.location = location;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = LocalDateTime.parse(manufacture, formatter);
        this.partStatus = partStatus;
    }

    /**
     * Constructor for a part.
     * @param partType Type of part.
     * @param location Location of the part.
     * @param manufacture Date the part was manufactured.
     * @param partStatus Status of the part.
     */
    public Part(final PartType partType, final Location location, final String manufacture, final PartStatus partStatus) {
        this.partType = partType;
        this.location = location;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = LocalDateTime.parse(manufacture, formatter);
        this.partStatus = partStatus;
    }

    /**
     * Constructor for a part.
     * @param partType Type of part.
     * @param location Location of the part.
     * @param partStatus Status of the part.
     */
    public Part(final PartType partType, final Location location, final PartStatus partStatus) {
        this.partType = partType;
        this.location = location;
        this.manufacture = LocalDateTime.now();
        this.partStatus = partStatus;
    }
}
