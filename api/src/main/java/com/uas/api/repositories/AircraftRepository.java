package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {

}
