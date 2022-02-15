package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;

import java.util.List;

public interface PartService {

    List<PartStockLevelDTO> getPartsAtLowStock();

    List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations();
}
