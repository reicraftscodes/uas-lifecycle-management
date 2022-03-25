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
    private long orderID;

    private String supplierEmail;

    private String generationTime;

    private Location deliveryLocation;

    private List<StockToOrders> partOrders;

    private double totalCost;
}
