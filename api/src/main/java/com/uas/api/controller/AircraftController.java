package com.uas.api.controller;

import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.services.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    /**
     * Aircraft service used to communicate with the db about the aircraft table.
     */
    private final AircraftService aircraftService;

    /**
     * Constructor.
     * @param aircraftService Aircraft service for db communication.
     */
    @Autowired
    public AircraftController(final AircraftService aircraftService) {
        this.aircraftService = aircraftService;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<UserAircraftDTO>> getUserAircraft(@PathVariable("id") final int userId) {

        List<UserAircraftDTO> userAircraftDTOs = aircraftService.getAircraftForUser(userId);
        return ResponseEntity.ok(userAircraftDTOs);
    }
}
