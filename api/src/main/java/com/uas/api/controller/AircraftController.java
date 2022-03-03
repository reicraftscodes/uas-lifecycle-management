package com.uas.api.controller;

import com.uas.api.models.dtos.AircraftAddHoursOperationalDTO;
import com.uas.api.models.dtos.AircraftHoursOperationalDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.services.AircraftServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/aircraft")
@CrossOrigin("http://localhost:3000/")
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
     * Gets the cumulative total repairs for each platform.
     * @return list of integers which represent the number of repairs done to each platform.
     */
    @GetMapping("/time-operational")
    public ResponseEntity <AircraftHoursOperationalDTO> getHoursOperational() {
        List<Integer> hoursOperational = aircraftService.getHoursOperational();
        AircraftHoursOperationalDTO aircraftTotalRepairsDTO = new AircraftHoursOperationalDTO(hoursOperational);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }

    /**
     * Updates the hours operational of an aircraft.
     * @return response entity indicating success/failure.
     */
    @PostMapping("/time-operational")
    public ResponseEntity<?> updateHoursOperational(@RequestBody final AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO) {
        Aircraft aircraft = aircraftService.updateHoursOperational(aircraftAddHoursOperationalDTO);
        List<Integer> hoursOperational = new ArrayList<>();
        hoursOperational.add(aircraft.getHoursOperational());
        
        AircraftHoursOperationalDTO aircraftHoursOperationalDTO = new AircraftHoursOperationalDTO(hoursOperational);

        return ResponseEntity.ok(aircraftHoursOperationalDTO);
    }

}
