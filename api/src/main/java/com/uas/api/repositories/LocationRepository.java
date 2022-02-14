package com.uas.api.repositories;

import com.uas.api.models.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

    List<Location> findAll();
    Optional<Location> findLocationByLocationName(String locationName);
}
