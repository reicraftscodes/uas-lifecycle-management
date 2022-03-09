package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {

    @Query(value = "SELECT SUM(price) FROM parttypes WHERE partID = ANY(SELECT PartID FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Double getTotalPartCostofAircraft(@Param("tailNumber") String tailNumber);

}
