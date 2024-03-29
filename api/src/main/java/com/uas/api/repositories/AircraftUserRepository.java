package com.uas.api.repositories;

import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.AircraftUser;
import com.uas.api.models.entities.AircraftUserKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface AircraftUserRepository extends JpaRepository<AircraftUser, AircraftUserKey>  {

    /**
     * Retrieve all aircraft assigned to a user.
     * @param userId the id of the user.
     * @return a list of AircraftUser.
     *
     */
    List<AircraftUser> findAllByUser_Id(long userId);

    /**
     * Get the aircraft with the provided tail number and assigned user.
     * @param tailNumber The tail number of the aircraft.
     * @param userId The user Id.
     * @return The user aircraft.
     */
    Optional<AircraftUser> findByAircraftAndUserId(Aircraft tailNumber, Long userId);

}
