package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.dtos.PartTypeFailureTimeDTO;

import java.util.HashMap;
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

}
