package com.uas.api.services;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AircraftService {
    /**
     * Adds an aircraft to the db.
     * @param requestData The json body turned into a hashmap
     * @return returns a string with any errors encountered adding the aircraft.
     */
    String addAircraftFromJson(AircraftAddNewDTO requestData);

    /** Calculates the total number of repairs for an aircraft.
     * @param tailNumber of the aircraft.
     * @return the number of repairs for a specific aircraft.
     */
    Integer calculateTotalRepairs(String tailNumber);
    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    List<AircraftUserDTO> getAircraftForUser(long userID);

    /**
     *  Updates the aircraft flight time hours.
     * @param aircraft The aircraft the flight time is being updated for.
     * @param flyTime The hours to be added to the flight time.
     */
    void updateAircraftFlyTime(Aircraft aircraft, int flyTime);
    /**
     * Gets the number of flytimehours as a list.
     * @return the list.
     */
    List<Integer> getFlyTimeHours();

    /**
     * Updates the number of operational hours.
     * @param aircraftAddFlyTimeHoursDTO the aircraft and the hours.
     * @return the number of hours total.
     */
    AircraftFlyTimeHoursDTO updateFlyTimeHours(AircraftAddFlyTimeHoursDTO aircraftAddFlyTimeHoursDTO);
    /**
     * Gets a list of platform status dto objects, with useful data.
     * @return the dto list.
     */
    List<PlatformStatusDTO> getPlatformStatus();

    /**
     * Gets the number of Aircraft with parts needing repair.
     * @return the integer.
     */
    Integer getNumberOfAircraftWithPartsNeedingRepair();

    /**
     * Gets a list of the platform status containing the name,
     * location and its operational status.
     * @return the list.
     */
    PlatformStatusAndroidFullDTO getPlatformStatusAndroid();

    /**
     * Gets a list of all aircraft in the database.
     * @return returns a list of aircraft DTO objects.
     */
    List<AircraftDTO> getAllAircraft();

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
     * Method for creating an aircraft DTO with just their repair and parts cost.
     * @param aircraftId the aircraft id.
     * @return returns aircraft dto.
     */
    AircraftCostsOverviewDTO getAircraftForCEOReturnMinimisedIdParam(String aircraftId) throws NotFoundException;

    /**
     * Used to update the user flytime of an aircraft in the database.
     * @param tailNumber The tail number of the aircraft that the hours are being updated for.
     * @param userId The user Id whose personal flight time is being updated.
     * @param flyTime The fly time to be added to the hours field.
     */
    void updateUserAircraftFlyTime(Aircraft tailNumber, long userId, int flyTime) throws NotFoundException;
    /**
     * Updates the given aircrafts' status.
     * @param aircraftStatusDTO A dto with the aircraft being updates and the status it is being changed to.
     * @return a response entity with and message if there was any problems.
     */
    ResponseEntity<?> updateAircraftStatus(UpdateAircraftStatusDTO aircraftStatusDTO) throws NotFoundException;

    /**
     * Gets the parts and their status for the aircraft given.
     * @param tailNumber The aircraft that the parts are being searched for.
     * @return Returns a response entity with a body containing the list of parts with their statuses.
     */
    ResponseEntity<?> getAircraftParts(String tailNumber) throws NotFoundException;

    /**
     * Updates an aircraft with a new part.
     * @param aircraftPartDTO a dto with the part being replaced and the new part number.
     * @return a response entity with and message if there was any problems.
     */
    ResponseEntity<?> updateAircraftPart(UpdateAircraftPartDTO aircraftPartDTO) throws NotFoundException;



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

    /**
     * Gets a filtered aircraft list.
     * @param locations the locations to be included in the search.
     * @param platformStatuses the platform statuses to be included in the search.
     * @return a list of AircraftDTOs that match the search criteria.
     */
    List<AircraftDTO> getFilteredAircraftList(List<String> locations, List<String> platformStatuses);

    AircraftDTO getAircraft(final String tailNumber) throws NotFoundException;
}
