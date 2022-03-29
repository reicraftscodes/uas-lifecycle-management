package com.uas.api.controller;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parts")
@CrossOrigin(origins = "http://localhost:3000")
public class PartsController {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartsController.class);
    /**
     * Communicated with the db about the parts table.
     */
    private final PartService partService;

    /**
     * Stock control service for communication between controller and DB.
     */
    private final StockControlService stockControlService;
    /**
     * Constructor.
     * @param partService Required Service.
     * @param stockControlService required service.
     */
    @Autowired
    public PartsController(final PartService partService, final StockControlService stockControlService) {
        this.partService = partService;
        this.stockControlService = stockControlService;
    }

    /**
     *  Post request for /parts/add which adds a part from the json.
     * @param requestData The json from the request turned into a hashmap.
     * @return returns a response entity of success or an error with the error message.
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> addPart(@RequestBody final AddPartDTO requestData) throws NotFoundException {
        partService.addPartFromJSON(requestData);
        return new ResponseEntity<>("{\"response\":\"Success\"}", HttpStatus.OK);
    }

    /**
     * Checks for low stock levels (40%>).
     * @return list of parts with low stock & response entity.
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<PartStockLevelDTO>> getPartsAtLowStock() throws NotFoundException {
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
        stockControlService.addMoreStock(moreStockRequest);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Checks for part stock levels at all locations.
     * @return list of parts stock levels at all locations & response entity.
     */
    @GetMapping("/stock")
    public ResponseEntity<List<LocationStockLevelsDTO>> getPartsStockAtAllLocations() throws Exception {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = partService.getPartStockLevelsForAllLocations();
        return ResponseEntity.ok(locationStockLevelsDTOs);
    }
    /**
     * Checks for part stock levels at location.
     * @param location the name of the location.
     * @return list of parts stock levels at location & response entity.
     */
    @GetMapping("/location/stock")
    public ResponseEntity<List<PartStockLevelDTO>> getPartsStockLevelsAtLocation(final @RequestParam("location") String location) throws NotFoundException {
        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartStockLevelsAtLocation(location);
        return ResponseEntity.ok(partStockLevelDTOs);
    }
    /**
     * Get mapping to retrieve all the failure times for all the parts.
     * @return list containing part names and failure times.
     */
    @GetMapping("/failuretime")
    public ResponseEntity<?> getFailureTime() {
        List<PartTypeFailureTimeDTO> failureTimes = partService.getFailureTime();
        return ResponseEntity.ok(failureTimes);
    }

    /**
     * Get mapping to retrieve all the top N most common failing parts.
     * @param topN the number of results to return.
     * @return list containing the most common failing parts and their cost.
     */
    @GetMapping("/most-failing/{topN}")
    public ResponseEntity<List<PartRepairsDTO>> getPartsMostFailing(@PathVariable("topN") final int topN) throws NotFoundException {
        List<PartRepairsDTO> partRepairsDTOs = partService.getMostCommonFailingParts(topN);
        return ResponseEntity.ok(partRepairsDTOs);
    }

    /**
     * Gets all the parts that are unassigned to aircraft for a specific part type.
     * @param partType The type of part being searched for.
     * @return returns a response entity with ok response and a body with a list of part numbers or an error response with an error message.
     */
    @PostMapping("/get-by-type")
    public ResponseEntity<?> getPartsAvailableByParttype(@RequestBody final long partType) {
        return ResponseEntity.ok(partService.availablePartsForParttype(partType));
    }

    /**
     * Get all parts.
     * @return a list of part dtos.
     */
    @GetMapping("/all")
    public ResponseEntity<List<PartDTO>> getAllParts() throws NotFoundException {
        List<PartDTO> partDTOs = partService.getAllParts();
        return ResponseEntity.ok(partDTOs);
    }

    /**
     * Updates the status of a given part.
     * @param updatePartStatusDTO A dto containing partID and a new part status.
     * @return A response entity with an appropriate body and status depending on the outcome.
     */
    @PostMapping("update-part-status")
    public ResponseEntity<?> updatePartStatus(@RequestBody UpdatePartStatusDTO updatePartStatusDTO){
        String result = partService.updatePartStatus(updatePartStatusDTO.getPartID(), updatePartStatusDTO.getPartStatus());
        if (result.equals("Success")) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
