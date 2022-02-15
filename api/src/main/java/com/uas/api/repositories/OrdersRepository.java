package com.uas.api.repositories;

import com.uas.api.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

}
