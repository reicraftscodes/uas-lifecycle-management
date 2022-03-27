package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftPart;
import com.uas.api.models.entities.enums.PartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftPartRepository extends JpaRepository<AircraftPart, String> {

    AircraftPart findAircraftPartByPart_PartNumber(Long partNumber);
    /**
     *  Finds all parts associated with an aircraft.
     * @param aircraft The aircraft the parts are being searched for.
     * @return A list of all part entities.
     */
    List<AircraftPart> findAircraftPartsByAircraft(Aircraft aircraft);

    List<AircraftPart> findAircraftPartsByPartStatus(PartStatus partStatus);

}
