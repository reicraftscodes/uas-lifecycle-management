package com.uas.api.controller;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.services.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    private final PartService partService;

    @Autowired
    public MainController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/api/parts/low-stock")
    public ResponseEntity<List<PartStockLevelDTO>> getPartsAtLowStock() {
        List<PartStockLevelDTO> partLowStockLevelDTOs = partService.getPartsAtLowStock();
        return ResponseEntity.ok(partLowStockLevelDTOs);
    }

    @GetMapping("/api/parts/stock")
    public ResponseEntity<List<LocationStockLevelsDTO>> getPartsStockAtAllLocations() {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = partService.getPartStockLevelsForAllLocations();
        return ResponseEntity.ok(locationStockLevelsDTOs);
    }

}
