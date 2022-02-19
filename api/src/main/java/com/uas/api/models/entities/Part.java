package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartStatus;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Column(name = "FlyTimeHours")
    private int FlyTimeHours;

    public Part(PartType partType, Aircraft aircraft, Location location, PartStatus partStatus){
        this.partType = partType;
        this.aircraft = aircraft;
        this.location = location;
        LocalDateTime dateTime = LocalDateTime.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = dateTime;
        this.partStatus = partStatus;
    }

    public Part(PartType partType, Aircraft aircraft, Location location, String manufacture, PartStatus partStatus) {
        this.partType = partType;
        this.aircraft = aircraft;
        this.location = location;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = LocalDateTime.parse(manufacture, formatter);
        this.partStatus = partStatus;
    }

    public Part(PartType partType, Location location, String manufacture, PartStatus partStatus){
        this.partType = partType;
        this.location = location;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.manufacture = LocalDateTime.parse(manufacture, formatter);
        this.partStatus = partStatus;
    }

    public Part(PartType partType, Location location, PartStatus partStatus){
        this.partType = partType;
        this.location = location;
        this.manufacture = LocalDateTime.now();
        this.partStatus = partStatus;
    }
}
