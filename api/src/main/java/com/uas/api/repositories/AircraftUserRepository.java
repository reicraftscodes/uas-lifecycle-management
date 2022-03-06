package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.AircraftUserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftUserRepository extends JpaRepository<AircraftUser, AircraftUserKey>  {

    /**
     * Retrieve all aircraft assigned to a user.
     * @param userId the id of the user.
     * @return a listr of AircraftUser.
     *
     */
    List<AircraftUser> findAllByUser_Id(long userId);

    AircraftUser findAircraftUsersByAircraft_TailNumber(String tailNumber);

}
