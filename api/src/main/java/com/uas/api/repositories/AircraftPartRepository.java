package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftPartRepository extends JpaRepository<AircraftPart, String> {

    AircraftPart findAircraftPartByPart_PartNumber(Long partNumber);
    List<AircraftPart> findAircraftPartsByAircraft(Aircraft aircraft);
}
