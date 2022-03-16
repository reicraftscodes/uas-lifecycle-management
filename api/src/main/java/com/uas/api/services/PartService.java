package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartRepairsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.dtos.PartTypeFailureTimeDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Part;

import java.util.HashMap;
import java.util.List;

public interface PartService {
    /**
     * Gets a list of all parts with low stock.
     * @return the list.
     */
    List<PartStockLevelDTO> getPartsAtLowStock();

    /**
     * Gets a list of all parts with low stock at given location.
     * @param locationName the name of the location.
     * @return the list of part stock level dtos.
     */
    List<PartStockLevelDTO> getPartStockLevelsAtLocation(String locationName);

    /**
     * Gets a list of all location part stock levels.
     * @return the list.
     */
    List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations();

    /**
     * Adds a part to the db from a json request.
     * @param requestData a hashmap of the json request data.
     * @return returns an error message if it occurs or if not a blank string.
     */
    String addPartFromJSON(HashMap<String, String> requestData);

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
    List<PartRepairsDTO> getMostCommonFailingParts(int topN);

    /**
     *  Finds all the parts associated with an aircraft.
     * @param aircraft The aircraft with the parts we are searching for.
     * @return returns a list of parts associated with that aircraft.
     */
    List<Part> findPartsAssociatedWithAircraft(Aircraft aircraft);

    /**
     * Updates a list of parts with the flight time specified.
     * @param parts The list of parts to update.
     * @param flyTime The flight time to be added to the parts flight time.
     */
    void updatePartFlyTime(List<Part> parts, int flyTime);

    /**
     * Gets all availble parts for a specific part type.
     * @param partType The part type being searched for.
     * @return a list of part numbers that are available.
     */
    List<String> availablePartsForParttype(long partType);


}
