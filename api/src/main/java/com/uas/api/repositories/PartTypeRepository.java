package com.uas.api.repositories;

import com.uas.api.models.entities.PartType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartTypeRepository extends JpaRepository<PartType, Integer> {
    /**
     * Find the part type by the id.
     * @param id part id.
     * @return the part type.
     */
    PartType findPartTypeById(long id);

}
