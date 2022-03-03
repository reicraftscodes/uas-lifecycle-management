package com.uas.api.controller;

import com.uas.api.models.dtos.UserAircraftDTO;
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
    public AircraftController(final AircraftService aircraftService,final PartService partService, final UserService userService) {
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

    @PostMapping("/log-flight")
    public ResponseEntity<?> updateFlightHours(@RequestBody HashMap<String, String> request){
        //{
        //    "aircraft":"G-001",
        //    "flyTime":12
        //}
        String error = null;
        Optional<Aircraft> aircraft = aircraftService.findAircraftById(request.get("aircraft"));

        if(aircraft.isPresent()){
            List<Part> parts = partService.findPartsAssociatedWithAircraft(aircraft.get());
            try {
                int hoursInput = Integer.parseInt(request.get("flyTime"));

                if(hoursInput < 0){
                    error = "Fly time value cannot be negative!";
                } else {
                    partService.updatePartFlyTime(parts, hoursInput);
                }
            } catch(Exception e){
                error = "Fly time value isn't integer!";
            }
        } else {
            error = "Aircraft not found!";
        }

        if(error==null){
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body("response: "+error);
        }

    }
}
