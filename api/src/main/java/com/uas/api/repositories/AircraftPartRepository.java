package com.uas.api.repositories;

import com.uas.api.models.entities.AircraftPart;
import com.uas.api.models.entities.enums.PartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftPartRepository extends JpaRepository<AircraftPart, String> {

    /**
     *  Finds all parts associated with an aircraft.
     * @param partNumber The part number of the AircraftPart's Part property.
     * @return An AircraftPart with a part with that part number.
     */
    AircraftPart findAircraftPartByPart_PartNumber(Long partNumber);

    /**
     *  Finds all parts associated with an aircraft.
     * @param tailNumber The aircraft tailNumber the parts are being searched for.
     * @return A list of all part entities.
     */
    List<AircraftPart> findAircraftPartsByAircraft_TailNumber(String tailNumber);

    /**
     *  Finds all parts associated with an aircraft with a specific partStatus.
     * @param partStatus The status of the part.
     * @return A list of part entities with a partStatus equal to the parameter.
     */
    List<AircraftPart> findAircraftPartsByPartStatus(PartStatus partStatus);

}
