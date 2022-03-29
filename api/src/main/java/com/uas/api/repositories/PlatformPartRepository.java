package com.uas.api.repositories;

import com.uas.api.models.entities.PlatformParts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformPartRepository extends JpaRepository<PlatformParts, Long> {


}
