package com.uas.api.repositories;

import com.uas.api.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
     /**
      * Used to get an order without knowning its assigned orderID yet. It will display the most recent order that also
      *  matches the location, total cost, and supplier email to be sure.
      * @param location The location.
      * @param totalCost The total cost of the searched for order.
      * @param supplierEmail The supplier email of the order being searched for.
      * @return The most recent order added that matches the inputs.
      */
     @Query(value = "SELECT * FROM Orders WHERE LocationName=:location AND TotalCost=:totalCost AND SupplierEmail=:supplierEmail ORDER BY OrderID DESC LIMIT 1", nativeQuery = true)
     Orders findByAttributes(@Param("location") String location, @Param("totalCost") double totalCost, @Param("supplierEmail") String supplierEmail);
}
