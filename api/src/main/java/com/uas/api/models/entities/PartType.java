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
@Table(name="PartTypes")
public class PartType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="PartID")
    private Long id;

//    @Enumerated(EnumType.STRING)
    //TODO: fix enum
    @Column(name="PartType")
    private String partName;

//    @Enumerated(EnumType.STRING)
    //TODO: fix enum
    @Column(name="PlatformType")
    private String platformType;

    @Column(name="Price")
    private BigDecimal price;

    @Column(name="Weight")
    private Long weight;

    // Typical failure time in hours
    @Column(name="TypicalFailureTime")
    private Long typicalFailureTime;

}
