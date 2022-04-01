package com.uas.api.repositories;

import com.uas.api.models.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LocationRepository extends JpaRepository<Location, String> {
    /**
     * Find all locations in DB.
     * @return list of locations.
     */
    List<Location> findAll();

    /**
     * Find a location by name, return empty if null.
     * @param locationName location to search by name.
     * @return optional containing location or null.
     */
    Optional<Location> findLocationByLocationName(String locationName);
}
