package com.uas.api.controller;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MainController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    /**
     * Part service for communication between controller and DB.
     */
    private final PartService partService;
    /**
     * Stock control service for communication between controller and DB.
     */
    private final StockControlService stockControlService;

    /**
     * Constructor.
     * @param partService required service.
     * @param stockControlService required service.
     */
    @Autowired
    public MainController(final PartService partService, final StockControlService stockControlService) {
        this.partService = partService;
        this.stockControlService = stockControlService;
    }

    /**
     * Checks for low stock levels (40%>).
     * @return list of parts with low stock & response entity.
     */
    @GetMapping("/api/parts/low-stock")
    public ResponseEntity<List<PartStockLevelDTO>> getPartsAtLowStock() {
        List<PartStockLevelDTO> partLowStockLevelDTOs = partService.getPartsAtLowStock();
        return ResponseEntity.ok(partLowStockLevelDTOs);
    }
    /**
     * A post mapping that allows a user to request more stock.
     * @param moreStockRequest request body.
     * @return response entity with response.
     */
    @PostMapping("/stockrequest")
    public ResponseEntity<?> requestMoreStock(@RequestBody final MoreStockRequest moreStockRequest) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LOGGER.info("Request for more stock made at: " + localDateTime + " by user: user");
        boolean confirmed = stockControlService.addMoreStock(moreStockRequest);
        if (confirmed) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Failed to save stock request!");
        }
    }

    /**
     * Checks for part stock levels at all locations.
     * @return list of parts stock levels at all locations & response entity.
     */
    @GetMapping("/api/parts/stock")
    public ResponseEntity<List<LocationStockLevelsDTO>> getPartsStockAtAllLocations() {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = partService.getPartStockLevelsForAllLocations();
        return ResponseEntity.ok(locationStockLevelsDTOs);
    }
    @GetMapping("/api/parts/failuretime")
    public ResponseEntity<?> getFailureTime() {
        partService.getFailureTime();
        return ResponseEntity.ok("Yay!");
    }

}
