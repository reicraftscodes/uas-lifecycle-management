package com.uas.api.repositories;

import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.enums.PartName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    /**
     * Count all by location name and part type name.
     * @param location location.
     * @param partName part name.
     * @return count.
     */
    int countAllByLocation_LocationNameAndPartType_PartName(String location, PartName partName);
}