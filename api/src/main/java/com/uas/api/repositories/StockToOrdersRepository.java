package com.uas.api.repositories;

import com.uas.api.models.entities.StockToOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockToOrdersRepository extends JpaRepository<StockToOrders, Integer> {
    /**
     * Finds all Stock requests from a specific orderID.
     * @param orderID The order id for requested parts.
     * @return returns a list of stockToOrders objects.
     */
    @Query(value = "SELECT * FROM stocktoorders WHERE OrderID = :orderID", nativeQuery = true)
    List<StockToOrders> findAllByOrderID(@Param("orderID") long orderID);

}
