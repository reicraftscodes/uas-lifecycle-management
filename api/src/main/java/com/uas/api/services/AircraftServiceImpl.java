package com.uas.api.services;

import com.uas.api.models.dtos.AircraftAddHoursOperationalDTO;
import com.uas.api.models.dtos.AircraftHoursOperationalDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
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
     */
    @Autowired
    public AircraftServiceImpl(final AircraftRepository aircraftRepository, final LocationRepository locationRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
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

        //Changes the json platform status from a string to an enum.
        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        try {
            PlatformStatus.valueOf(requestData.get("platformStatus"));
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
}
