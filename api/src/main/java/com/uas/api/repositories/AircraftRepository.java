package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {
    /**
     * Uses SQL query to get the total part cost of a given aircraft.
     * @param tailNumber The tailnumber of the aircraft that the cost is being calculated for.
     * @return returns a double of the total cost.
     */
    @Query(value = "SELECT SUM(price) FROM parttypes WHERE partID = ANY(SELECT PartID FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Double getTotalPartCostofAircraft(@Param("tailNumber") String tailNumber);

    /**
     * Uses SQL query to get the total part cost of all aircraft.
     * @return returns the total cost.
     */
    @Query(value = "SELECT SUM(price) FROM parttypes WHERE partID = ANY(SELECT PartID FROM parts WHERE AircraftTailNumber IS NOT NULL)", nativeQuery = true)
    Double getTotalPartCostofAllAircraft();


}
