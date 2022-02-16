package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface AircraftRepository extends CrudRepository<Aircraft, String> {

}
