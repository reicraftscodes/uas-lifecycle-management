package com.uas.api.controller;

import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.services.AircraftService;
import com.uas.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/aircraft")
@CrossOrigin(origins = "http://localhost:3000")
public class AircraftController {
    /**
     * Aircraft service used to communicate with the db about the aircraft table.
     */
    private final AircraftService aircraftService;
    /**
     * User service for communication between controller and DB.
     */
    private final UserService userService;

    /**
     * Constructor.
     * @param aircraftService Aircraft service for db communication.
     * @param userService User service for communication between controller and DB.
     */
    @Autowired
    public AircraftController(final AircraftService aircraftService, final UserService userService) {
        this.aircraftService = aircraftService;
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




        return ResponseEntity.ok("");
    }
}
