package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartName;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PartTypes")
public class PartType {
    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PartID")
    private Long id;
    /**
     * Part name.
     */
    @Column(name = "PartType")
    private PartName partName;
    /**
     * Cost.
     */
    @Column(name = "Price")
    private BigDecimal price;
    /**
     * Weight.
     */
    @Column(name = "Weight")
    private Long weight;

    // Typical failure time in hours
    /**
     * Failure time.
     */
    @Column(name = "TypicalFailureTime")
    private Long typicalFailureTime;

}
