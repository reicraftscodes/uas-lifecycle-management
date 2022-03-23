package com.uas.api.repositories;

import com.uas.api.models.entities.StockToOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockToOrdersRepository extends JpaRepository<StockToOrders, Integer> {

    @Query(value="SELECT * FROM stocktoorders WHERE OrderID=:orderID",nativeQuery = true)
    List<StockToOrders> findAllByOrderID(@Param("orderID") long OrderID);
}
