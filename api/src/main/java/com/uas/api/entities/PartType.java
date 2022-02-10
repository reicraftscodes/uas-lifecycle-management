package com.uas.api.entities;

import com.uas.api.entities.enums.PartName;
import com.uas.api.entities.enums.PlatformType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name="PartTypes")
public class PartType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="PartID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="PartType")
    private PartName partName;

    @Enumerated(EnumType.STRING)
    @Column(name="PlatformType")
    private PlatformType platformType;

    @Column(name="Price")
    private BigDecimal price;

    @Column(name="Weight")
    private Long weight;

    // Typical failure time in hours
    @Column(name="TypicalFailureTime")
    private Long typicalFailureTime;

}
