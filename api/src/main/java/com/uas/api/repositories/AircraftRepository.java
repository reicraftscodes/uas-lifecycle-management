package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {

}
