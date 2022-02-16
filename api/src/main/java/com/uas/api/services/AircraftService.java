package com.uas.api.services;

import com.uas.api.controller.AircraftController;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class AircraftService {
    private final AircraftRepository aircraftRepository;
    private final LocationRepository locationRepository;
    private static final Logger log = LoggerFactory.getLogger(AircraftService.class);

    @Autowired
    public AircraftService(AircraftRepository aircraftRepository, LocationRepository locationRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
    }

    public void addAircraft(Aircraft aircraft){
        aircraftRepository.save(aircraft);
    }

    public String addAircraftFromJson(HashMap<String,String> requestData) throws Exception {
        String errorMessage = null;

        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        switch (requestData.get("platformStatus")) {
            case "Design" : break;
            case "Production" : platformStatus = PlatformStatus.PRODUCTION; break;
            case "Operation" : platformStatus = PlatformStatus.OPERATION; break;
            case "Repair" : platformStatus = PlatformStatus.REPAIR; break;
            default: errorMessage = "Invalid platform status."; break;
        }

        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));
        if(location.isEmpty()){
            errorMessage = "Location not found.";
        }

        PlatformType platformType = PlatformType.PLATFORM_A;
        switch (requestData.get("platformType")) {
            case "Platform_A" : break;
            case "Platform_B" : platformType = PlatformType.PLATFORM_B; break;
            default: errorMessage = "Invalid platform type."; break;
        }

        if(errorMessage == null) {
            Aircraft aircraft = new Aircraft(requestData.get("tailNumber"), location.get(), platformStatus, platformType);
            try {
                aircraftRepository.save(aircraft);
                log.info("Aircraft added by user. Tailnumber:"+requestData.get("tailNumber")
                        +" Location:" +requestData.get("location")
                        +" Platform Status:"+requestData.get("platformStatus")
                        +" Platform Type:"+requestData.get("platformType"));
            } catch(Exception e) {
                errorMessage = e.getMessage();
            }
        }
        return errorMessage;
    }
}
