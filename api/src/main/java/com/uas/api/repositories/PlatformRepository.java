package com.uas.api.repositories;

import com.uas.api.models.entities.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long> {

    /**
     * Get a list of platforms that are compatible with a part.
     * @param partID the part id/number.
     * @return a list of compatible platforms.
     */
    @Query(value = "select * from platforms where platformid in (select platformid from PlatformParts where parttypeid = (select parttypeid from parts where partid = :partID))", nativeQuery = true)
    List<Platform> findCompatiblePlatformTypesForPart(@Param("partID") long partID);





}
