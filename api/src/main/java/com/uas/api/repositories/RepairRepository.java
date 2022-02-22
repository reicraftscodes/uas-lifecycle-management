package com.uas.api.repositories;

import com.uas.api.models.entities.Repair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {

    @Query(value = "select r.part.partNumber as partNumber, count(r.part.partNumber) as repairCount, sum(r.cost) as totalCost from Repair r group by r.part.partNumber")
    Page<Map<Object, Object>> findPartsWithMostRepairsAndTheirCost(PageRequest pageRequest);

}
