package com.uas.api.repositories;

import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    /**
     * Count all by location name and part type name.
     * @param location location.
     * @param part part.
     * @return count.
     */
    int countAllByPartAndLocation(Part part, Location location);

    List<Stock> getAllByPart_PartNumber(long partNumber);
}
