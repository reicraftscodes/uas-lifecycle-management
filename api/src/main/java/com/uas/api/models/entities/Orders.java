package com.uas.api.models.entities;

import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Orders")
@Getter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private int orderID;

    @Column(name = "TotalCost")
    private double totalCost;

    @Column(name = "OrderDateTime")
    private Timestamp orderDateTime;

}
