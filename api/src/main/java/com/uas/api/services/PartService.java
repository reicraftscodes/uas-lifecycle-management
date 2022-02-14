package com.uas.api.services;

import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.entities.enums.PartName;

import java.util.List;

public interface PartService {

    List<PartStockLevelDTO> getPartsAtLowStock();
}
