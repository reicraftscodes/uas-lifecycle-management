package com.uas.api.controller;


import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.services.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    private final LocationRepository locationRepository;
    private final AircraftService aircraftService;

    @Autowired
    public AircraftController(LocationRepository locationRepository, AircraftService aircraftService) {
        this.locationRepository = locationRepository;
        this.aircraftService = aircraftService;
    }


    @PostMapping(value = "/add", consumes = "application/json")
    void addAircraft(@RequestBody HashMap<String, String> requestData){
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.

        aircraftService.addAircraftFromJson(requestData);

    }
}
