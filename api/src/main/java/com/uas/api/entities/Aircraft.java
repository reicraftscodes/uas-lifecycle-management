package com.uas.api.entities;

import com.uas.api.entities.enums.PlatformStatus;
import com.uas.api.entities.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Aircraft")
public class Aircraft {

    @Id
    @Column(name="TailNumber")
    private String tailNumber;

    @ManyToOne
    @JoinColumn(name = "LocationName")
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name="PlatformStatus")
    private PlatformStatus platformStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="PlatformType")
    private PlatformType platformType;
}
