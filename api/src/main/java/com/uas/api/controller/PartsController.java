package com.uas.api.controller;

import com.uas.api.exceptions.InvalidDTOAttributeException;
import com.uas.api.models.dtos.*;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parts")
@CrossOrigin(origins = "https://uastest.herokuapp.com")
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
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    ResponseEntity<?> addPart(@RequestBody final AddPartDTO requestData) throws NotFoundException {
        partService.addPartFromJSON(requestData);
        return new ResponseEntity<>("{\"response\":\"Success\"}", HttpStatus.OK);
    }

    /**
     * Get mapping to transfer parts from one stock location to another.
     * @param locationName the name of the location to transfer parts from.
     * @param newLocationName the name of the location to transfer parts to.
     * @param partName the name of the parts to send.
     * @param quantity the number of the parts to send.
     * @return response entity indicating success/failure.
     */
    @GetMapping("/transfer/{locationName}/{newLocationName}/{partName}/{quantity}")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> transferPart(@PathVariable("locationName") final String locationName, @PathVariable("newLocationName") final String newLocationName, @PathVariable("partName") final String partName, @PathVariable("quantity") final int quantity) {
        String msg = partService.transferPart(locationName, newLocationName, partName, quantity);
        String response = "{\"response\":\"" + msg + "\"}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get mapping to delete
     * @param locationName the name of the location to delete parts from.
     * @param partName the name of the parts to delete.
     * @param quantity the number of the parts to delete.
     * @return response entity indicating success/failure.
     */
    @GetMapping("/delete/{locationName}/{partName}/{quantity}")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> deletePart(@PathVariable("locationName") final String locationName, @PathVariable("partName") final String partName, @PathVariable("quantity") final int quantity) {
        String msg = partService.deletePart(locationName, partName, quantity);
        String response = "{\"response\":\"" + msg + "\"}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Checks for low stock levels (40%>).
     * @return list of parts with low stock & response entity.
     */
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_COO')")
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
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
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
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_COO')")
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
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_COO')")
    public ResponseEntity<List<PartStockLevelDTO>> getPartsStockLevelsAtLocation(final @RequestParam("location") String location) throws NotFoundException {
        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartStockLevelsAtLocation(location);
        return ResponseEntity.ok(partStockLevelDTOs);
    }
    /**
     * Get mapping to retrieve all the failure times for all the parts.
     * @return list containing part names and failure times.
     */
    @GetMapping("/failuretime")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CTO')")
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
    @PreAuthorize("hasRole('ROLE_USER_CTO')")
    public ResponseEntity<List<PartRepairsDTO>> getPartsMostFailing(@PathVariable("topN") final int topN) throws NotFoundException {
        List<PartRepairsDTO> partRepairsDTOs = partService.getMostCommonFailingParts(topN);
        return ResponseEntity.ok(partRepairsDTOs);
    }

    /**
     * Gets all the parts that are unassigned to aircraft for a specific part type.
     * @param partType The type of part being searched for.
     * @return returns a response entity with ok response and a body with a list of part numbers or an error response with an error message.
     */
    @GetMapping("/get-by-type/{id}")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> getPartsAvailableByParttype(@PathVariable("id") final long partType) {
        return ResponseEntity.ok(partService.availablePartsForParttype(partType));
    }

    /**
     * Get all parts.
     * @return a list of part dtos.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
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
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updatePartStatus(@RequestBody final UpdatePartStatusDTO updatePartStatusDTO) throws NotFoundException, InvalidDTOAttributeException {
        partService.updatePartStatus(updatePartStatusDTO.getPartID(), updatePartStatusDTO.getPartStatus());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Updates the price of a given part.
     * @param updatePartPriceDTO A dto containing the partID and new price.
     * @return A response entity with an appropriate body and status depending on the outcome.
     */
    @PostMapping("update-part-price")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updatePartPrice(@RequestBody final UpdatePartPriceDTO updatePartPriceDTO) throws NotFoundException {
        partService.updatePartPrice(updatePartPriceDTO.getPartID(), updatePartPriceDTO.getPrice());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Updates the weight of a given part.
     * @param updatePartWeightDTO A dto containing the partID and new weight.
     * @return A response entity with an appropriate body and status depending on the outcome.
     */
    @PostMapping("update-part-weight")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updatePartWeight(@RequestBody final UpdatePartWeightDTO updatePartWeightDTO) throws NotFoundException {
        partService.updatePartWeight(updatePartWeightDTO.getPartID(), updatePartWeightDTO.getWeight());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Update the failure time of a given part.
     * @param updatePartFailureTimeDTO A dto containing the partID and new failure time.
     * @return A response entity with an appropriate body and status depending on the outcome.
     */
    @PostMapping("update-part-failure-time")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updateFailureTime(@RequestBody final UpdatePartFailureTimeDTO updatePartFailureTimeDTO) throws NotFoundException {
        partService.updateFailureTime(updatePartFailureTimeDTO.getPartID(), updatePartFailureTimeDTO.getFailureTime());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Gets the information on a given part.
     * @param partNumber The partID of the part being searched for.
     * @return returns a PartInfoDTO of part infomation.
     */
    @PostMapping("get-part")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> getPart(@RequestBody final long partNumber) {
        try {
            PartInfoDTO partInfo = partService.getPartInfo(partNumber);
            return ResponseEntity.ok(partInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all part stock orders.
     * @return a list of stock order dtos.
     */
    @GetMapping("/stock-order/all")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<List<StockOrderDTO>> getAllStockOrders() throws NotFoundException {
        List<StockOrderDTO> stockOrderDTOs = stockControlService.getAllPreviousStockOrders();
        return ResponseEntity.ok(stockOrderDTOs);
    }
}
