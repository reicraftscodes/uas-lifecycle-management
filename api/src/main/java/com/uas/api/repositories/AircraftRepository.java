package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.enums.PlatformStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {
    /**
     * Uses SQL query to get the total part cost of a given aircraft.
     * @param tailNumber The tailnumber of the aircraft that the cost is being calculated for.
     * @return returns a double of the total cost.
     */
    @Query(value = "SELECT SUM(price) FROM parts WHERE partid = ANY(SELECT partid FROM AircraftPart WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Double getTotalPartCostofAircraft(@Param("tailNumber") String tailNumber);


    /**
     * Uses SQL query to get the total part cost of all aircraft.
     * @return returns the total cost.
     */
    @Query(value = "SELECT SUM(price) FROM parts WHERE partid = ANY(SELECT partid FROM AircraftPart WHERE AircraftTailNumber IS NOT NULL)", nativeQuery = true)
    Double getTotalPartCostofAllAircraft();

    /**
     * Finds aircraft by platform status.
     * @param platformStatus
     * @return the list of aircraft with that status.
     */
    List<Aircraft> findAircraftsByPlatformStatus(PlatformStatus platformStatus);

    /**
     * Find all aircraft that match the search criteria.
     * @param locations the locations to be included in the search.
     * @param platformStatuses the platform statuses to be included in the search.
     * @return the filtered list of aircraft.
     * LocationName in (:locations) and
     */
    @Query(value = "select * from aircraft where PlatformStatus in (:platformStatuses) and LocationName in (:locations)", nativeQuery = true)
    List<Aircraft> findAllByLocationsAndPlatformStatus(@Param("locations") List<String> locations, @Param("platformStatuses") List<String> platformStatuses);
}
