package com.uas.api.services;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;

import java.util.List;
import java.util.Optional;

public interface AircraftService {
    /**
     * Adds an aircraft to the db.
     * @param requestData The json body turned into a hashmap
     * @return returns a string with any errors encountered adding the aircraft.
     */
    String addAircraftFromJson(AircraftAddNewDTO requestData);

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
    /**
     * Gets a list of platform status dto objects, with useful data.
     * @return the dto list.
     */
    List<PlatformStatusDTO> getPlatformStatus();

    /**
     * Gets a list of the platform status containing the name,
     * location and its operational status.
     * @return the list.
     */
    PlatformStatusAndroidFullDTO getPlatformStatusAndroid();

    /**
     * Gets a list of all aircraft in the database.
     * @return returns a list of aircraft objects.
     */
    List<Aircraft> getAllAircraft();

    /**
     * Gets the value of all aircrafts total repair cost.
     * @return total repair cost.
     */
    double getAllAircraftTotalRepairCost();

    /**
     * Gets the value of all aircraft part cost.
     * @return total part cost.
     */
    double getAllTotalAircraftPartCost();

    /**
     * Gets the total value of parts for a given aircraft.
     * @param aircraft The aircraft parts are being summed for.
     * @return The total amount spent on parts.
     */
    double getTotalPartCostForSpecificAircraft(Aircraft aircraft);

    /**
     * Gets the total repair cost for a given aircraft.
     * @param aircraft The aircraft repairs are being found for.
     * @return The total amount spent on repairs.
     */
    double getTotalRepairCostForSpecificAircraft(Aircraft aircraft);

    /**
     * Method for creating a list of aircraft dtos and their parts and costs to be returned to the user.
     * @return returns a list of aircraft dtos.
     */
    List<AircraftCostsDetailDTO> getAircraftForCEOReturn();

    /**
     * Method for creating a list of aircraft objects with just their repair and parts cost.
     * @return returns a list of aircraft dtos.
     */
    List<AircraftCostsOverviewDTO> getAircraftForCEOReturnMinimised();

    /**
     * Used to update the user flytime of an aircraft in the database.
     * @param tailNumber The tail number of the aircraft that the hours are being updated for.
     * @param userId The user Id whose personal flight time is being updated.
     * @param flyTime The fly time to be added to the hours field.
     */
    void updateUserAircraftFlyTime(String tailNumber, long userId, int flyTime);

    /**
     * Used to assign the user to an aircraft by creating an AircraftUser from existing user and aircraft.
     * @param aircraftUserKeyDTO The DTO containing the aircraftUserKey for creation of the AircraftUser.
     * @return returns a AircraftUserDTO.
     */
    AircraftUserDTO assignUserToAircraft(AircraftUserKeyDTO aircraftUserKeyDTO);

    /**
     * Gets a filtered platform details list.
     * @param locations the locations to be included in the search.
     * @param platformStatuses the platform statuses to be included in the search.
     * @return a list of PlatformStatusDTOs that match the search criteria.
     */
    List<PlatformStatusDTO> getFilteredPlatformStatusList(List<String> locations, List<String> platformStatuses);
}
