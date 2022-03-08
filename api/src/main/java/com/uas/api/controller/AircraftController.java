package com.uas.api.controller;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Part;
import com.uas.api.services.AircraftService;
import com.uas.api.services.PartService;
import com.uas.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
@CrossOrigin(origins = "http://localhost:3000")
public class AircraftController {
    /**
     * Aircraft service used to communicate with the db about the aircraft table.
     */
    private final AircraftService aircraftService;
    /**
     * part service used for communication with the db about the part table.
     */
    private final PartService partService;
    /**
     * User service for communication between controller and DB.
     */
    private final UserService userService;

    /**
     * Constructor.
     * @param aircraftService Aircraft service for db communication.
     * @param partService
     * @param userService User service for communication between controller and DB.
     */
    @Autowired
    public AircraftController(final AircraftService aircraftService, final PartService partService, final UserService userService) {
        this.aircraftService = aircraftService;
        this.partService = partService;
        this.userService = userService;
    }

    /**
     * Postmapping for requests to add aircraft to the database.
     * @param requestData JSON data from the request body turned into a HashMap.
     * @return A response to the request with either a confirmation or the error received.
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> addAircraft(@RequestBody final HashMap<String, String> requestData) {
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.

        String result = aircraftService.addAircraftFromJson(requestData);

        if (result == null) {
            return ResponseEntity.ok().body("{\"response\":\"Success\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"response\":\"" + result + "\"}");
        }
    }

    /**
     * Get mapping request for retrieving all aircraft assigned to a user.
     * @param userId the id of the user.
     * @return response entity with response.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserAircraft(@PathVariable("id") final long userId) {
        if (!userService.userExistsById(userId)) {
            return ResponseEntity.badRequest().body("Failed to retrieve aircraft for user because user does not exist.");
        } else {
            List<UserAircraftDTO> userAircraftDTOs = aircraftService.getAircraftForUser(userId);
            return ResponseEntity.ok(userAircraftDTOs);
        }
    }

    /**
     * Post mapping used for updating the aircrafts and the parts associated with that aircrafts flight hours.
     * @param request takes json request body for the aircraft tailnumber and flytime to be logged.
     * @return returns a response with ok for no errors or a bad request with a body with the error message.
     */
    @PostMapping(value = "/log-flight", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateFlightHours(@RequestBody final LogFlightDTO request) {
        // A request body example that a post would have
        //{
        //    "aircraft":"G-001",
        //    "flyTime":12
        //}
        String error = null;
        //Gets the aircraft entity from the post request body
        Optional<Aircraft> aircraft = aircraftService.findAircraftById(request.getAircraft());

        //checks that an aircraft has been found from the aircraft input and if not sets the error variable.
        if (aircraft.isPresent()) {
            //gets all parts associated with the aircraft and stores them in the list.
            List<Part> parts = partService.findPartsAssociatedWithAircraft(aircraft.get());
            //Uses a try and catch statement to check if the user input hours is an integer.
            try {
                int hoursInput = request.getFlyTime();

                //checks the user input is positive and if not sets error variable
                if (hoursInput < 0) {
                    error = "Fly time value cannot be negative!";
                } else {
                    //updates the part flight hours for all parts associated with the aircraft.
                    partService.updatePartFlyTime(parts, hoursInput);
                    //updates the aircraft flight hours
                    aircraftService.updateAircraftFlyTime(aircraft.get(), hoursInput);
                }
            } catch (Exception e) {
                error = "Fly time value isn't integer!";
            }
        } else {
            error = "Aircraft not found!";
        }

        //checks for errors and if no errors then returns and okay response
        if (error == null) {
            return ResponseEntity.ok("");
        } else {
            //if there are errors then it returns a bad request with a response of the error.
            return ResponseEntity.badRequest().body("response: " + error);
        }

    }
    /**
     * Gets a the cumulative total repairs for each platform.
     * @return list of integers which represent the number of repairs done to each platform.
     */
    @GetMapping("/total-repairs/{tailNumber}")
    public ResponseEntity<AircraftTotalRepairsDTO> getTotalRepairs(@PathVariable final String tailNumber) {
        Integer aircraftRepairsCount = aircraftService.calculateTotalRepairs(tailNumber);
        AircraftTotalRepairsDTO aircraftTotalRepairsDTO = new AircraftTotalRepairsDTO(aircraftRepairsCount);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }

    @GetMapping("/needing-repair")
    public Integer getNumberOfAircraftWithPartsNeedingRepair() {
        Integer aircraftNeedingRepair = aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        return aircraftNeedingRepair;
    }
    /**
     * Gets the cumulative total repairs for each platform.
     * @return list of integers which represent the number of repairs done to each platform.
     */
    @GetMapping("/time-operational")
    public ResponseEntity<AircraftHoursOperationalDTO> getHoursOperational() {
        List<Integer> hoursOperational = aircraftService.getHoursOperational();
        AircraftHoursOperationalDTO aircraftTotalRepairsDTO = new AircraftHoursOperationalDTO(hoursOperational);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }

    /**
     * Updates the hours operational of an aircraft.
     * @param aircraftAddHoursOperationalDTO request body.
     * @return response entity indicating success/failure.
     */
    @PostMapping("/time-operational")
    public ResponseEntity<?> updateHoursOperational(@RequestBody final AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO) {
        AircraftHoursOperationalDTO aircraft = aircraftService.updateHoursOperational(aircraftAddHoursOperationalDTO);
        return ResponseEntity.ok(aircraft);
    }

}
