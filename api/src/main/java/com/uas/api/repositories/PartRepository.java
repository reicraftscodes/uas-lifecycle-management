package com.uas.api.repositories;

import com.uas.api.models.entities.Part;
import com.uas.api.repositories.projections.PartFailureTimeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PartRepository extends JpaRepository<Part, Integer> {

    /**
     * Find a part by the partNumber.
     * @param partNumber The part number of the part being searched for.
     * @return returns a part if found.
     */
    Optional<Part> findPartBypartNumber(long partNumber);

    /**
     * Gets a list of the parts and their failure times.
     * @return the list of parts.
     */
    @Query(value = "SELECT PartTypes.PartTypeName, Parts.TypicalFailureTime from Parts inner join PartTypes on Parts.PartTypeId = PartTypes.PartTypeId", nativeQuery = true)
    List<PartFailureTimeProjection> findAllProjectedBy();



    /**
     * Finds all the parts in the db that are not assigned to an aircraft for a specific part type.
     * @param partID the part type being searched for.
     * @return returns a list of part numbers that are available.
     */
    @Query(value = "SELECT PartID FROM parts WHERE AircraftTailNumber IS NULL AND partID=:partID", nativeQuery = true)
    List<String> findAllAvailbleByType(@Param("partID") long partID);

//    /**
//     * Finds all the parts in the db that have a specific.
//     * @param partStatus the part status being searched for.
//     * @return returns a list of part numbers that are available.
//     */
//    List<Part> findAllByPartStatus(PartStatus partStatus);

}
