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

        boolean error = false;

        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        switch (requestData.get("platformStatus")) {
            case "Design" : break;
            case "Production" : platformStatus = PlatformStatus.PRODUCTION; break;
            case "Operation" : platformStatus = PlatformStatus.OPERATION; break;
            case "Repair" : platformStatus = PlatformStatus.REPAIR; break;
            default: error = true; break;
        }

        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));

        PlatformType platformType = PlatformType.PLATFORM_A;
        switch (requestData.get("platformType")) {
            case "Platform_A" : break;
            case "Platform_B" : platformType = PlatformType.PLATFORM_B; break;
            default: error = true; break;
        }

        if(!error) {
            System.out.println(platformType);
            Aircraft aircraft = new Aircraft(requestData.get("tailNumber"), location.get(), platformStatus, platformType);

            System.out.println(aircraft.getPlatformType());
            aircraftService.addAircraft(aircraft);
        }



    }
}
