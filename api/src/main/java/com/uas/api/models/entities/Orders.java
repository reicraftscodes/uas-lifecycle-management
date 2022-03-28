package com.uas.api.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Orders")
@Getter
@NoArgsConstructor
public class Orders {
    /**
     * Order id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private int orderID;
    /**
     * Location name.
     */
    @ManyToOne
    @JoinColumn(name = "LocationName", referencedColumnName = "LocationName")
    private Location locationName;
    /**
     * The email address the order is being sent to.
     */
    @Column(name = "SupplierEmail")
    private String supplierEmail;
    /**
     * Total cost of order.
     */
    @Column(name = "TotalCost")
    private double totalCost;
    /**
     * Date and time of order.
     */
    @Column(name = "OrderDateTime")
    private Timestamp orderDateTime;

    /**
     * Constructor.
     * @param locationName required
     * @param supplierEmail required.
     * @param totalCost required.
     * @param orderDateTime required.
     */
    public Orders(final Location locationName, final String supplierEmail, final double totalCost, final Timestamp orderDateTime) {
        this.locationName = locationName;
        this.supplierEmail = supplierEmail;
        this.totalCost = totalCost;
        this.orderDateTime = orderDateTime;
    }
}
