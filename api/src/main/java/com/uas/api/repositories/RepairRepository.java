package com.uas.api.repositories;

import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.Repair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface RepairRepository extends JpaRepository<Repair, Long> {

    /**
     * Get the parts with the most repairs and their total cost from the db.
     * @param pageRequest the page request.
     * @return map of objects containing the part number, their repair count and their total cost.
     */
    @Query(value = "select r.part.partNumber as partNumber, count(r.part.partNumber) as repairCount, sum(r.cost) as totalCost from Repair r group by r.part.partNumber")
    Page<Map<Object, Object>> findPartsWithMostRepairsAndTheirCost(PageRequest pageRequest);

    /**
     * Find all parts by aircraft and platform type?
     * @param tailNumber of aircraft.
     * @return the list of repairs?
     */
    List<Repair> findAllByPart_Aircraft_TailNumber(String tailNumber);

    /**
     * Find all repairs of a specific part.
     * @param part The part repairs are being found for.
     * @return returns a list of repairs for the given part.
     */
    List<Repair> findAllByPart(Part part);

    /**
     * Gets the total repair cost for a specific aircraft using an sql query.
     * @param tailNumber the tailnumber that has the repairs associated with it.
     * @return returns the total cost of repairs.
     */
    @Query(value = "SELECT sum(cost) FROM Repairs WHERE PartNumber= ANY (SELECT PartNumber FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Double findTotalRepairCostForAircraft(@Param("tailNumber") String tailNumber);

    /**
     * Gets the total cost of repairs for all aircraft using an sql query.
     * @return the total cost of repairs for all aircraft.
     */
    @Query(value = "SELECT sum(cost) FROM Repairs WHERE PartNumber= ANY (SELECT PartNumber FROM parts)", nativeQuery = true)
    Double findTotalRepairCostForAllAircraft();

    /**
     * Get the total repair count for an aircraft.
     * @param tailNumber the tail number of the aircraft.
     * @return the aircraft repair count.
     */
    @Query(value = "SELECT COUNT(RepairID) FROM Repairs WHERE PartNumber IN (SELECT PartNumber FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Integer findRepairsCountForAircraft(@Param("tailNumber") String tailNumber);

}
