package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Aircraft")
public class Aircraft {

    @Id
    @Column(name="TailNumber")
    private Long tailNumber;

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
