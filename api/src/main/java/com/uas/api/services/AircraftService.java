package com.uas.api.services;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class AircraftService {
    private final AircraftRepository aircraftRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public AircraftService(AircraftRepository aircraftRepository, LocationRepository locationRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
    }

    public void addAircraft(Aircraft aircraft){
        aircraftRepository.save(aircraft);
    }

    public void addAircraftFromJson(HashMap<String,String> requestData){
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
        if(location.isEmpty()){
            error = true;
        }

        PlatformType platformType = PlatformType.PLATFORM_A;
        switch (requestData.get("platformType")) {
            case "Platform_A" : break;
            case "Platform_B" : platformType = PlatformType.PLATFORM_B; break;
            default: error = true; break;
        }

        if(!error) {
            Aircraft aircraft = new Aircraft(requestData.get("tailNumber"), location.get(), platformStatus, platformType);
            aircraftRepository.save(aircraft);
        }

    }
}
