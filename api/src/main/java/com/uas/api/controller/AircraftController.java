package com.uas.api.controller;

import com.uas.api.models.dtos.AircraftTotalRepairsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.services.AircraftServiceImpl;
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
     * Gets a the cumulative total repairs for each platform.
     * @return list of integers which represent the number of repairs done to each platform.
     */
    @GetMapping("/total-repairs")
    public ResponseEntity <AircraftTotalRepairsDTO> getTotalRepairs() {
        List<Integer> aircraftRepairsList = aircraftService.calculateTotalRepairs();
        AircraftTotalRepairsDTO aircraftTotalRepairsDTO = new AircraftTotalRepairsDTO(aircraftRepairsList);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }
}
