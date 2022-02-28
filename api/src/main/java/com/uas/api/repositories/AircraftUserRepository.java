package com.uas.api.repositories;

import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.AircraftUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftUserRepository extends JpaRepository<AircraftUser, AircraftUserKey>  {

    List<AircraftUser> findAllByUser_Id(long userId);
}
