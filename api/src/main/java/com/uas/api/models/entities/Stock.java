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
     * PartTypeID.
     */
    @ManyToOne
    @JoinColumn(name = "PartID", referencedColumnName = "PartID")
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
    @JoinColumn(name = "locationName")
    private Location location;

    /**
     *  Constructor for a stock.
     * @param part part in stock.
     * @param stockQuantity quantity of the part in stock.
     * @param location location of the parts in stock.
     */
    public Stock(final Part part, final long stockQuantity, final Location location) {
        this.part = part;
        this.stockQuantity = stockQuantity;
        this.location = location;
    }
}
