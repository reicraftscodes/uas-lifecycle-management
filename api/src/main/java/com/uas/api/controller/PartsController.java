package com.uas.api.controller;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.StringToEnumConverter;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import com.uas.api.repositories.PartTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/parts")
public class PartsController {

    private final PartRepository partRepository;
    private final PartTypeRepository partTypeRepository;
    private final AircraftRepository aircraftRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public PartsController(PartRepository partRepository, PartTypeRepository partTypeRepository, AircraftRepository aircraftRepository, LocationRepository locationRepository) {
        this.partRepository = partRepository;
        this.partTypeRepository = partTypeRepository;
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
    }

    @PostMapping(value="/add", consumes = "application/json")
    ResponseEntity<?> addPart(@RequestBody HashMap<String,String> requestData) {
        StringToEnumConverter stringToEnumConverter = new StringToEnumConverter();

        PartType partType = partTypeRepository.findPartTypeById(Long.parseLong(requestData.get("partType")));
        Optional<Aircraft> aircraft = aircraftRepository.findById(requestData.get("aircraft"));
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));

        PartStatus partStatus = PartStatus.OPERATIONAL;
        try {
            partStatus = stringToEnumConverter.stringToPartStatus(requestData.get("partStatus"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        Part part = new Part(partType,aircraft.get(),location.get(),partStatus);

        

        System.out.println(part.getManufacture());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
