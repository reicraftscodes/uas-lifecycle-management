package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import org.springframework.data.repository.CrudRepository;

public interface AircraftRepository extends CrudRepository<Aircraft, String> {

}
