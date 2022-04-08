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
    //int findAllByPartAndLocation(Part part, Location location);

    Stock findByPartAndLocation(Part part, Location location);
    /**
     * Get a list of stock for a part.
     * @param partNumber the part number.
     * @return a list of stock items.
     */
    List<Stock> getAllByPart_PartNumber(long partNumber);

    /**
     * Finds a stock entity by location and part name.
     * @param location the location of the stock.
     * @param partName the name of the part.
     * @return a stock entity.
     */
    Stock findByLocationAndPart_PartName(Location location, String partName);

}
