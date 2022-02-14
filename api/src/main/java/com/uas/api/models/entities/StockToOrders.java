package com.uas.api.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "StockToOrders")
@Getter
@NoArgsConstructor
public class StockToOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StockToOrderID")
    private int stockToOrderID;
    @ManyToOne
    @JoinColumn(name = "OrderID", referencedColumnName = "OrderID")
    private Orders orderID;
    @ManyToOne
    @JoinColumn(name = "PartID", referencedColumnName = "PartID")
    private PartType partID;
    @Column(name = "Quantity")
    private int quantity;

    public StockToOrders(Orders orderID, PartType partID, int quantity) {
        this.orderID = orderID;
        this.partID = partID;
        this.quantity = quantity;
    }
}
