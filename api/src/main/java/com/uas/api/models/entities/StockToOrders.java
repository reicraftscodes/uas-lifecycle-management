package com.uas.api.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "StockToOrders")
@Getter
@NoArgsConstructor
public class StockToOrders {
    /**
     * Stock to order id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockToOrderID")
    private int stockToOrderID;
    /**
     * Order id.
     */
    @ManyToOne
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID")
    private Orders orderID;
    /**
     * Part type id.
     */
    @ManyToOne
    @JoinColumn(name = "PartID", referencedColumnName = "PartID")
    private PartType partID;
    /**
     * Quantity of part.
     */
    @Column(name = "Quantity")
    private int quantity;

    /**
     * Constructor.
     * @param orderID required.
     * @param partID required.
     * @param quantity required.
     */
    public StockToOrders(final Orders orderID, final PartType partID, final int quantity) {
        this.orderID = orderID;
        this.partID = partID;
        this.quantity = quantity;
    }
}
