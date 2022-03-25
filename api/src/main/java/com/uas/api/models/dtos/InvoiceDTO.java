package com.uas.api.models.dtos;

import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.StockToOrders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class InvoiceDTO {
    private final String orderID;

    private final String supplierEmail;

    private final String generationTime;

    private final Location deliveryLocation;

    private final List<StockToOrders> partOrders;

    private final double totalCost;
}
