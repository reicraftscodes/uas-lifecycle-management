package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class StockOrderDTO {

    /**
     * Location name.
     */
    private String locationName;
    /**
     * The email address the order is being sent to.
     */
    private String supplierEmail;
    /**
     * Total cost of order.
     */
    private double totalCost;
    /**
     * Date and time of order.
     */
    private Timestamp orderDateTime;
    /**
     * The part name.
     */
    private String partName;
    /**
     * Quantity of part.
     */
    private int quantity;
}
