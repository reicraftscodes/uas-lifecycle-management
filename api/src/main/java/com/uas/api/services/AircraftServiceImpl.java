package com.uas.api.services;

import com.uas.api.models.dtos.PlatformStatusDTO;
import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.dtos.AircraftAddHoursOperationalDTO;
import com.uas.api.models.dtos.AircraftHoursOperationalDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformAvailability;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AircraftServiceImpl implements AircraftService {
    /**
     *  Aircraft repository contains methods for communicating with aircraft table in db.
     */
    private final AircraftRepository aircraftRepository;
    /**
     * Contains methods for communication with the location table of the db.
     */
    private final LocationRepository locationRepository;
    /**
     * Contains methods for communication with the aircraft_user table of the db.
     */
    private final AircraftUserRepository aircraftUserRepository;
    /**
     * Contains methods for communication with the repair table of the db.
     */
    private final RepairRepository repairRepository;
    /**
     * Used to output logs of what the program is doing to the console.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AircraftServiceImpl.class);
    /**
     * The error message?
     */
    private String errorMessage = null;

    /**
     * The constructor.
     * @param aircraftRepository Repository used to modify aircraft data in db.
     * @param locationRepository Repository used to retrieve location data in db.
     * @param aircraftUserRepository Repository used to modify aircraft user data in db.
     * @param repairRepository
     */
    @Autowired
    public AircraftServiceImpl(final AircraftRepository aircraftRepository,
                               final LocationRepository locationRepository,
                               final AircraftUserRepository aircraftUserRepository,
                               final RepairRepository repairRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
        this.repairRepository = repairRepository;
        this.aircraftUserRepository = aircraftUserRepository;
    }

    /**
     * Used to save an aircraft to the database.
     * @param aircraft The aircraft entity to be saved.
     */
    public void addAircraft(final Aircraft aircraft) {
        aircraftRepository.save(aircraft);
    }

    /**
     * Takes the post request body and adds an aircraft from this.
     * @param requestData A hashmap of the json data. Enums made automatic object creation hard so a hashmap was used.
     * @return Returns a string which is null if the aircraft is successfully added but if there is an error this string is used so
     * it can be returned in the response body.
     */
    @Override
    public String addAircraftFromJson(final HashMap<String, String> requestData) {
        //Stores error messages and tracks if any errors have occured.
        String errorMessage = null;

        //Changes the json platform status from a string to an enum.
        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        try {
            platformStatus = PlatformStatus.valueOf(requestData.get("platformStatus"));
        } catch (Exception e) {
            errorMessage = "Invalid platform status.";
        }

        //Checks that the location entered exists and creates a location object.
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));
        if (location.isEmpty()) {
            errorMessage = "Invalid location not found.";
        }

        //Changes the json platform type to enum.
        PlatformType platformType = PlatformType.PLATFORM_A;
        switch (requestData.get("platformType")) {
            case "Platform_A" : break;
            case "Platform_B" : platformType = PlatformType.PLATFORM_B; break;
            default: errorMessage = "Invalid platform type."; break;
        }

        Optional<Aircraft> aircraftCheck = aircraftRepository.findById(requestData.get("tailNumber"));
        if (aircraftCheck.isPresent()) {
            errorMessage = "Invalid aircraft with specified tail number already present.";
        }

        //Checks if any errors have happened and if so doesn't save the aircraft to the db.
        if (errorMessage == null) {
            Aircraft aircraft = new Aircraft(requestData.get("tailNumber"), location.get(), platformStatus, platformType, 0);
            try {
                aircraftRepository.save(aircraft);
                //Logs the aircraft added to the console.
                LOG.info("Aircraft added by user. Tailnumber:" + requestData.get("tailNumber")
                        + " Location:" + requestData.get("location")
                        + " Platform Status:" + requestData.get("platformStatus")
                        + " Platform Type:" + requestData.get("platformType"));
            } catch (Exception e) {
                //Catches any other exceptions and sets the error message to them.
                errorMessage = e.getMessage();
            }
        }
        //Returns either null meaning no errors or an error message about what went wrong.
        return errorMessage;
    }

    /**
     * Optional finds aircraft from the db by the tailnumber id.
     * @param id The tailnumber id.
     * @return returns an aircraft.
     */
    @Override
    public Optional<Aircraft> findAircraftById(final String id) {

        return (aircraftRepository.findById(id));

    }

    /**
     * For each aircraft saved in the DB, get the hours operational and add it to the list.
     * @return returns a list of hours operational for each platform.
     */
    @Override
    public List<Integer> getHoursOperational() {
        List<Integer> hoursOperationalList = new ArrayList<>();
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        for (Aircraft aircraft: aircraftList
             ) {
            hoursOperationalList.add(aircraft.getHoursOperational());
        }
        return hoursOperationalList;
    }

    /**
     * Updates the number of hours an aircraft has been operational.
     * @param aircraftAddHoursOperationalDTO aircraft and the hours.
     * @return the updated hours.
     */
    @Override
    public AircraftHoursOperationalDTO updateHoursOperational(final AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO) {
        Aircraft aircraft = aircraftRepository.findById(aircraftAddHoursOperationalDTO.getTailNumber()).get();
        Integer hoursToAdd = aircraftAddHoursOperationalDTO.getHoursToAdd();
        List<Integer> hoursOperational = new ArrayList<>();
        if (aircraft.getHoursOperational() != null) {
            hoursToAdd += aircraft.getHoursOperational();

        }
        hoursOperational.add(hoursToAdd);
        aircraft.setHoursOperational(hoursToAdd);
        aircraftRepository.save(aircraft);
        return new AircraftHoursOperationalDTO(hoursOperational);

    }
    /**
     * Gets a list of platform status dto objects, which display an overall platform status of the aircraft.
     * @return the list of platform status dto objects.
     */
    @Override
    public List<PlatformStatusDTO> getPlatformStatus() {
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        List<PlatformStatusDTO> platformStatusDTOList = new ArrayList<>();
        //Until total repairs method is implemented use dummy data of 12.
        Integer totalCost = 12;

        for (Aircraft aircraft: aircraftList
             ) {
            PlatformStatusDTO platformStatusDTO = new PlatformStatusDTO(aircraft.getTailNumber(), aircraft.getFlyTimeHours(), aircraft.getPlatformStatus(), getAircraftAvailability(aircraft), totalCost);
            platformStatusDTOList.add(platformStatusDTO);
        }

        return platformStatusDTOList;
    }
    /**
     * Return a platform status object based on the result of if the aircraft is currently assigned.
     * @param aircraft the aircraft to check the availability of.
     * @return a platform status of assigned or unassigned.
     */
    @Override
    public PlatformAvailability getAircraftAvailability(final Aircraft aircraft) {
        try {
            Optional<AircraftUser> aircraftUser = Optional.ofNullable(aircraftUserRepository.findAircraftUsersByAircraft_TailNumber(aircraft.getTailNumber()).get(0));
            if (aircraftUser.get().getAircraft().getTailNumber().equals(aircraft.getTailNumber())) {
                return PlatformAvailability.ASSIGNED;
            } else {
                return PlatformAvailability.UNASSIGNED;
            }
        } catch (Exception e) {
            System.out.println("Error thrown");
            return PlatformAvailability.UNASSIGNED;
        }
    }

    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    public List<UserAircraftDTO> getAircraftForUser(final long userID) {
        List<AircraftUser> aircraftUsers = aircraftUserRepository.findAllByUser_Id(userID);
        List<UserAircraftDTO> userAircraftDTOs = new ArrayList<>();
        for (AircraftUser aircraftUser : aircraftUsers) {
            userAircraftDTOs.add(
                    new UserAircraftDTO(
                            aircraftUser.getAircraft().getTailNumber(),
                            aircraftUser.getAircraft().getLocation().getLocationName(),
                            aircraftUser.getAircraft().getPlatformStatus().getLabel(),
                            aircraftUser.getAircraft().getPlatformType().getName(),
                            aircraftUser.getUserFlyingHours(),
                            aircraftUser.getAircraft().getFlyTimeHours()));
        }
        return userAircraftDTOs;
    }

    /**
     * Used to update the flytime of an aircraft in the database.
     * @param aircraft The aircraft that the hours are being updated for.
     * @param flyTime The fly time to be added to the hours field.
     */
    public void updateAircraftFlyTime(final Aircraft aircraft, final int flyTime) {
        //the flytime currently in the database.
        int oldFlyTime = aircraft.getFlyTimeHours();

        //sets the new flytime to the new hours logged plus the old hours
        aircraft.setFlyTimeHours(oldFlyTime + flyTime);

        //saves to the db an update aircraft entity
        aircraftRepository.save(aircraft);
    }

    /**
     * Calculates the total number of repairs.
     * @return total repairs.
     */
    public List<Integer> calculateTotalRepairs() {

        Integer totalPlatA = repairRepository.findAllByPart_Aircraft_PlatformType(PlatformType.PLATFORM_A).size();
        Integer totalPlatB = repairRepository.findAllByPart_Aircraft_PlatformType(PlatformType.PLATFORM_B).size();

        List<Integer> totalRepairs = new ArrayList<>();
        totalRepairs.add(totalPlatA);
        totalRepairs.add(totalPlatB);

        return totalRepairs;
    }

}
