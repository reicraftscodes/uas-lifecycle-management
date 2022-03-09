package com.uas.api.repositories;

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
     * @return a list of AircraftUser.
     *
     */
    List<AircraftUser> findAllByUser_Id(long userId);
    /**
     * Retrieve all aircraft users that have been assigned to an Aircraft based on its tailNumber.
     * @param tailNumber the id of the Aircraft.
     * @return a list of AircraftUser.
     *
     */
    List<AircraftUser> findAircraftUsersByAircraft_TailNumber(String tailNumber);
}
