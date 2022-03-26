package com.uas.api.repositories;

import com.uas.api.models.entities.PartType;
import com.uas.api.repositories.projections.PartTypeFailureTimeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PartTypeRepository extends JpaRepository<PartType, Integer> {
    /**
     * Find the part type by the id.
     * @param id part id.
     * @return the part type.
     */
    Optional<PartType> findPartTypeById(long id);

    /**
     * Gets a list of the parts and their failure times.
     * @return the list of parts.
     */
    @Query(value = "SELECT PartType, TypicalFailureTime FROM PartTypes", nativeQuery = true)
    List<PartTypeFailureTimeProjection> findAllProjectedBy();

    /**
     * Get the part type name using the part number.
     * @param partNumber the part number of the part.
     * @return the part type name.
     */
    @Query(value = "SELECT PartType FROM PartTypes WHERE PartID = (SELECT PartID from Parts WHERE PartNumber = ?1)", nativeQuery = true)
    String getPartTypeByPartNumber(long partNumber);


}
