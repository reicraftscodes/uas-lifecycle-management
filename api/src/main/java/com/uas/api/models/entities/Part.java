package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartStatus;
import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
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
     * Manufacture date.
     */
    @Column(name = "Manufacture")
    private LocalDateTime manufacture;
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
    /**
     * Failure time.
     */
    @Column(name = "TypicalFailureTime")
    private Long typicalFailureTime;

    /**
     *  Constructor for a part.
     * @param partType Type of part.
     */
    public Part(final PartType partType) {
        this.partType = partType;
        this.manufacture = LocalDateTime.now();
    }



    /**
     * Constructor for a part.
     * @param partType Type of part.
     * @param price Price of the part.
     * @param weight Weight of the part.
     * @param typicalFailureTime Typical failure time of the part.
     */
    public Part(final PartType partType, final BigDecimal price, final long weight, final long typicalFailureTime) {
        this.partType = partType;
        this.price = price;
        this.weight = weight;
        this.typicalFailureTime = typicalFailureTime;
    }


}
