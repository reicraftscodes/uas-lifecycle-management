package com.uas.api.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
     * Total cost of order.
     */
    @Column(name = "TotalCost")
    private double totalCost;
    /**
     *  The email address of the supplier the order is going to.
     */
    @Column(name = "SupplierEmail")
    private String supplierEmail;
    /**
     * Date and time of order.
     */
    @Column(name = "OrderDateTime")
    private Timestamp orderDateTime;

    /**
     * Constructor.
     * @param locationName required.
     * @param totalCost required.
     * @param orderDateTime required.
     */
    public Orders(final Location locationName, final double totalCost, final String supplierEmail, final Timestamp orderDateTime) {
        this.locationName = locationName;
        this.totalCost = totalCost;
        this.supplierEmail = supplierEmail;
        this.orderDateTime = orderDateTime;
    }
}
