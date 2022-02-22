package com.uas.api.repositories;

import com.uas.api.models.entities.PartType;
import com.uas.api.repositories.projections.PartTypeFailureTimeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartTypeRepository extends JpaRepository<PartType, Integer> {
    /**
     * Find the part type by the id.
     * @param id part id.
     * @return the part type.
     */
    PartType findPartTypeById(long id);
    @Query("SELECT partName, typicalFailureTime FROM PartType")
    List<PartTypeFailureTimeProjection> findAllProjectedBy();

}
