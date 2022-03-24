package com.uas.api.repositories;

import com.uas.api.models.entities.AircraftPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftPartRepository extends JpaRepository<AircraftPart, String> {
}
