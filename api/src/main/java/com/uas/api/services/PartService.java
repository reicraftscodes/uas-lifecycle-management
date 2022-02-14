package com.uas.api.services;

import com.uas.api.models.dtos.PartStockLevelDTO;

import java.util.List;

public interface PartService {
    /**
     * Gets a list of all parts with low stock.
     * @return the list.
     */
    List<PartStockLevelDTO> getPartsAtLowStock();
}
