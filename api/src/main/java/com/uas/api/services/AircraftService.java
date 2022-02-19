package com.uas.api.services;

import com.uas.api.models.entities.Aircraft;

import java.util.HashMap;
import java.util.Optional;

public interface AircraftService {
    /**
     * Adds an aircraft to the db.
     * @param requestData The json body turned into a hashmap
     * @return returns a string with any errors encountered adding the aircraft.
     */
    String addAircraftFromJson(HashMap<String, String> requestData);

    Optional<Aircraft> findAircraftById(String Id);

}
