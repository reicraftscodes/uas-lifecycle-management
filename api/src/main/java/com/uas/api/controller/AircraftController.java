package com.uas.api.controller;

import com.uas.api.services.AircraftServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    /**
     * Aircraft service used to communicate with the db about the aircraft table.
     */
    private final AircraftServiceImpl aircraftService;

    /**
     * Constructor.
     * @param aircraftService Aircraft service for db communication.
     */
    @Autowired
    public AircraftController(final AircraftServiceImpl aircraftService) {
        this.aircraftService = aircraftService;
    }

    /**
     * Postmapping for requests to add aircraft to the database.
     * @param requestData JSON data from the request body turned into a HashMap.
     * @return A response to the request with either a confirmation or the error received.
     */
    @PostMapping(value = "/add", consumes = "application/json")
    ResponseEntity<?> addAircraft(@RequestBody final HashMap<String, String> requestData) {
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.
        String result = aircraftService.addAircraftFromJson(requestData);

        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Error adding aircraft. " + result);
        }
    }
}
