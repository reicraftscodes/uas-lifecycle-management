package com.uas.api.models.entities;

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
@Table(name = "Stock")
public class Stock {

    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockID")
    private Long id;

    /**
     * PartID.
     */
    @ManyToOne
    @JoinColumn(name = "partNumber", referencedColumnName = "partNumber")
    private Part part;

    /**
     * StockQuantity.
     */
    @Column(name = "StockQuantity")
    private Long stockQuantity;

    /**
     * Location.
     */
    @ManyToOne
    @JoinColumn(name = "LocationName")
    private Location location;

}
