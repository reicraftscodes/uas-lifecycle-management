package com.uas.api.repositories;

import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.enums.PartName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Integer> {

    /**
     * Find a part by the partNumber.
     * @param partNumber The part number of the part being searched for.
     * @return returns a part if found.
     */
    Optional<Part> findPartBypartNumber(long partNumber);

    /**
     * Count all by location name and part type name.
     * @param location location.
     * @param partName part name.
     * @return count.
     */
    int countAllByLocation_LocationNameAndPartType_PartName(String location, PartName partName);
}
