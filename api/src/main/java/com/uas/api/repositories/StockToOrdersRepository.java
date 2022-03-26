package com.uas.api.repositories;

import com.uas.api.models.entities.StockToOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockToOrdersRepository extends JpaRepository<StockToOrders, Integer> {
}
