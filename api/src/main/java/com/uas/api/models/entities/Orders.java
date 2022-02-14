package com.uas.api.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Orders")
@Getter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private int orderID;
    @ManyToOne
    @JoinColumn(name = "LocationName", referencedColumnName = "LocationName")
    private Location locationName;
    @Column(name = "TotalCost")
    private double totalCost;

    @Column(name = "OrderDateTime")
    private Timestamp orderDateTime;

    public Orders(Location locationName, double totalCost, Timestamp orderDateTime) {
        this.locationName = locationName;
        this.totalCost = totalCost;
        this.orderDateTime = orderDateTime;
    }
}
