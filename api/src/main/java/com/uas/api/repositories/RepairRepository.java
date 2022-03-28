package com.uas.api.repositories;

import com.uas.api.models.entities.AircraftPart;
import com.uas.api.models.entities.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface RepairRepository extends JpaRepository<Repair, Long> {

    /**
     * Get the parts with the most repairs and their total cost from the db.
     * @param limit the top number of repairs requested.
     * @return map of objects containing the part number, their repair count and their total cost.
     */
     @Query(value = "SELECT partid as partNumber, COUNT(partid) as repairCount, SUM(cost) as totalCost FROM Repairs group by partid order by repairCount DESC LIMIT :limit", nativeQuery = true)
     List<Map<Object, Object>> findPartsWithMostRepairsAndTheirCostWithLimit(@Param("limit") int limit);
    /**
     * Find all parts by aircraft and platform type?
     * @param tailNumber of aircraft.
     * @return the list of repairs?
     */
    List<Repair> findAllByAircraftPart_Aircraft_TailNumber(String tailNumber);

    /**
     * Find all repairs of a specific part.
     * @param part The part repairs are being found for.
     * @return returns a list of repairs for the given part.
     */
    List<Repair> findAllByAircraftPart(AircraftPart part);

    /**
     * Gets the total repair cost for a specific aircraft using an sql query.
     * @param tailNumber the tailnumber that has the repairs associated with it.
     * @return returns the total cost of repairs.
     */

    @Query(value = "SELECT sum(cost) FROM Repairs WHERE partid= ANY (SELECT partid FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Double findTotalRepairCostForAircraft(@Param("tailNumber") String tailNumber);

    /**
     * Gets the total cost of repairs for all aircraft using an sql query.
     * @return the total cost of repairs for all aircraft.
     */

    @Query(value = "SELECT sum(cost) FROM Repairs WHERE partid= ANY (SELECT partid FROM parts)", nativeQuery = true)
    Double findTotalRepairCostForAllAircraft();


    /**
     * Get the total repair count for an aircraft.
     * @param tailNumber the tail number of the aircraft.
     * @return the aircraft repair count.
     */
    @Query(value = "SELECT COUNT(RepairID) FROM Repairs WHERE partid IN (SELECT partid FROM parts WHERE AircraftTailNumber=:tailNumber)", nativeQuery = true)
    Integer findRepairsCountForAircraft(@Param("tailNumber") String tailNumber);

}
