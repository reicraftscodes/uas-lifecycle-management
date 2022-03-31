package com.uas.api.services;

import com.uas.api.exceptions.InvalidDTOAttributeException;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.AircraftPart;
import javassist.NotFoundException;

import java.util.List;

public interface PartService {
    /**
     * Gets a list of all parts with low stock.
     * @return the list.
     */
    List<PartStockLevelDTO> getPartsAtLowStock() throws NotFoundException;

    /**
     * Gets a list of all parts with low stock at given location.
     * @param locationName the name of the location.
     * @return the list of part stock level dtos.
     */
    List<PartStockLevelDTO> getPartStockLevelsAtLocation(String locationName) throws NotFoundException;

    /**
     * Gets a list of all location part stock levels.
     * @return the list.
     */
    List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations() throws NotFoundException;

    /**
     * Adds a part to the db from a json request.
     * @param requestData a hashmap of the json request data.
     */
    void addPartFromJSON(AddPartDTO requestData) throws NotFoundException;

    /**
     * Gets the part name and it's failure time from the part type table using a projection.
     * @return the part name (String) and it's failure time (long) (via projection)
     */
    List<PartTypeFailureTimeDTO> getFailureTime();

    /**
     * Get the top N most common failing parts.
     * @param topN the number of results to return.
     * @return the PartRepairsDTO list.
     */
    List<PartRepairsDTO> getMostCommonFailingParts(int topN) throws NotFoundException;

    /**
     * Updates a list of parts with the flight time specified.
     * @param parts The list of parts to update.
     * @param flyTime The flight time to be added to the parts flight time.
     */
    void updatePartFlyTime(List<AircraftPart> parts, int flyTime);

    /**
     * Update all flight hours.
     * @param request The DTO with request to update all flight hours.
     */
    void updateAllFlightHours(LogFlightDTO request) throws NotFoundException, InvalidDTOAttributeException;

    /**
     * Gets all availble parts for a specific part type.
     * @param partType The part type being searched for.
     * @return a list of part numbers that are available.
     */
    List<String> availablePartsForParttype(long partType);

    /**
     * Get a list of all parts.
     * @return a list of part dtos.
     */
    List<PartDTO> getAllParts() throws NotFoundException;

    /**
     * Updates a parts status in the aircraft part db table.
     * @param partNumber The part number of the part.
     * @param partStatus The status that the part is being updated to.
     * @return Returns the result of the status update.
     */
    void updatePartStatus(long partNumber, String partStatus) throws NotFoundException;

    /**
     * Updates a specified parts cost in the db.
     * @param partNumber The partID of the part having its price updated.
     * @param price The price it is being updated to.
     * @return Returns the result of the status update.
     */
    void updatePartPrice(long partNumber, double price) throws NotFoundException;

    /**
     * Updates a specific part weight in the db.
     * @param partNumber The partID of the part having its price updated.
     * @param weight The new weight.
     */
    void updatePartWeight(long partNumber, long weight) throws NotFoundException;

    /**
     * Updates the failure time of a specified part in the db.
     * @param partNumber The partID of the part having its failure time updated.
     * @param failureTime The new typical failure time.
     */
    void updateFailureTime(long partNumber, long failureTime) throws NotFoundException;

    /**
     * Gets the basic part information of a specific part.
     * @param partNumber The partID of the part being searched for.
     * @return A partInfoDTO.
     * @throws Exception Throws an exception if the part is not found.
     */
    PartInfoDTO getPartInfo(long partNumber) throws Exception;
}
