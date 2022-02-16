package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;

import java.util.List;

public interface PartService {
    /**
     * Gets a list of all parts with low stock.
     * @return the list.
     */
    List<PartStockLevelDTO> getPartsAtLowStock();

    /**
     * Gets a list of all location part stock levels.
     * @return the list.
     */
    List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations();
}
