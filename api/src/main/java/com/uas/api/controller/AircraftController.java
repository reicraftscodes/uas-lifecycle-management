package com.uas.api.controller;


import com.uas.api.models.entities.Aircraft;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.services.AircraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;
    private static final Logger log = LoggerFactory.getLogger(AircraftController.class);

    @Autowired
    public AircraftController(LocationRepository locationRepository, AircraftService aircraftService, AircraftRepository aircraftRepository) {
        this.aircraftService = aircraftService;
    }


    @PostMapping(value = "/add", consumes = "application/json")
    void addAircraft(@RequestBody HashMap<String, String> requestData) throws Exception {
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.

        log.info("Aircraft added by user. Tailnumber:"+requestData.get("tailNumber")
                +" Location:" +requestData.get("location")
                +" Platform Status:"+requestData.get("platformStatus")
                +" Platform Type:"+requestData.get("platformType"));

        boolean result = aircraftService.addAircraftFromJson(requestData);


    }

}
