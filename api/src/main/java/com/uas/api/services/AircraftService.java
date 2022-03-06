package com.uas.api.services;

import com.uas.api.models.dtos.PlatformStatusDTO;
import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.dtos.AircraftAddHoursOperationalDTO;
import com.uas.api.models.dtos.AircraftHoursOperationalDTO;
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
     * Calculates the total number of repairs.
     * @return the list of repairs?
     */
    List<Integer> calculateTotalRepairs();
    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    List<UserAircraftDTO> getAircraftForUser(long userID);

    /**
     *  Updates the aircraft flight time hours.
     * @param aircraft The aircraft the flight time is being updated for.
     * @param flyTime The hours to be added to the flight time.
     */
    void updateAircraftFlyTime(Aircraft aircraft, int flyTime);
    /**
     * Gets the number of hours operational as a list.
     * @return the list.
     */
    List<Integer> getHoursOperational();

    /**
     * Updates the number of operational hours.
     * @param aircraftAddHoursOperationalDTO the aircraft and the hours.
     * @return the number of hours total.
     */
    AircraftHoursOperationalDTO updateHoursOperational(AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO);

    List<PlatformStatusDTO> getPlatformStatus();
}
