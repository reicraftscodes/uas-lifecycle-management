package com.uas.api.repositories;

import com.uas.api.models.entities.AircraftPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
     *  Counts the number of unique aircraft there are with an aircraft part needing repair.
     * @return The number of unique aircraft needing repair.
     */
    @Query(value = "SELECT COUNT(DISTINCT AircraftTailNumber) FROM aircraftpart where PartStatus=\"Awaiting Repair\"", nativeQuery = true)
    Integer countUniqueAircraftWithPartsNeedingRepair();

}
