package com.uas.api.repositories;

import com.uas.api.models.entities.PartType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartTypeRepository extends JpaRepository<PartType, Integer> {
    PartType findPartTypeById(long id);
}
