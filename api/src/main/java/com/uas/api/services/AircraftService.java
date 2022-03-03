package com.uas.api.services;

import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.entities.Aircraft;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AircraftService {
    /**
     * Adds an aircraft to the db.
     * @param requestData The json body turned into a hashmap
     * @return returns a string with any errors encountered adding the aircraft.
     */
    String addAircraftFromJson(HashMap<String, String> requestData);

    /**
     * trys to find an aircraft from the database.
     * @param id The tailnumber id of the aircraft.
     * @return returns an aircraft object if found.
     */
    Optional<Aircraft> findAircraftById(String id);

    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    List<UserAircraftDTO> getAircraftForUser(long userID);

    void updateAircraftFlyTime(Aircraft aircraft, int flyTime);
}
