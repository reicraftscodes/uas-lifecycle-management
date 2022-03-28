package com.uas.api.models.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "partid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partNumber;
    /**
     * Part Type ID.
     */
    @OneToOne
    @JoinColumn(name = "parttypeid", referencedColumnName = "parttypeid")
    private PartType partType;

    /**
     * Part Name.
     */
    @Column(name = "partname")
    private String partName;
    /**
     * Manufacture date.
     */
    @Column(name = "manufacture")
    private LocalDateTime manufacture;
    /**
     * Cost.
     */
    @Column(name = "price")
    private BigDecimal price;
    /**
     * Weight.
     */
    @Column(name = "weight")
    private Long weight;
    /**
     * Failure time.
     */
    @Column(name = "typicalfailuretime")
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
     * @param partName Name of the part.
     */
    public Part(final PartType partType, final String partName, final BigDecimal price, final long weight, final long typicalFailureTime) {
        this.partType = partType;
        this.partName = partName;
        this.price = price;
        this.weight = weight;
        this.typicalFailureTime = typicalFailureTime;
    }
    /**
     * Constructor for a part.
     * @param partType Type of part.
     * @param price Price of the part.
     * @param weight Weight of the part.
     * @param typicalFailureTime Typical failure time of the part.
     * @param partName Name of the part.
     * @param manufacture Timestamp of part.
     */
    public Part(final PartType partType, final String partName, final LocalDateTime manufacture, final BigDecimal price, final long weight, final long typicalFailureTime) {
        this.partType = partType;
        this.partName = partName;
        this.manufacture = manufacture;
        this.price = price;
        this.weight = weight;
        this.typicalFailureTime = typicalFailureTime;
    }
}
