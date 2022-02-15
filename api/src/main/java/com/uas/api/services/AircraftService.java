package com.uas.api.services;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.repositories.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AircraftService {
    private final AircraftRepository aircraftRepository;

    @Autowired
    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }
    
    public void addAircraft(Aircraft aircraft){
        aircraftRepository.save(aircraft);
    }
}
