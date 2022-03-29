package com.uas.api.models.dtos;

import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.StockToOrders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceDTO {
    /**
     * The order ID.
     */
    private long orderID;
    /**
     * The email the invoice is going to be sent to.
     */
    private String supplierEmail;
    /**
     * When the order was generated.
     */
    private String generationTime;
    /**
     * Where the order should be delivered.
     */
    private Location deliveryLocation;
    /**
     * A list of StockToOrders DTOs containing parts and quantities required.
     */
    private List<StockToOrders> partOrders;
    /**
     *  The total cost of the whole order.
     */
    private double totalCost;
}
