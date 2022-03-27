package com.uas.api.services;

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
    void updateAllFlightHours(LogFlightDTO request) throws NotFoundException;

    /**
     * Gets all availble parts for a specific part type.
     * @param partType The part type being searched for.
     * @return a list of part numbers that are available.
     */
    List<String> availablePartsForParttype(long partType);


}
