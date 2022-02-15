package com.uas.api.services;

import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PartServiceImpl implements PartService {
    /**
     * Repository for communication between API and part table in db.
     */
    private final PartRepository partRepository;
    /**
     * Repository for communication between API and location table in db.
     */
    private final LocationRepository locationRepository;

    // This will probably change.
    /**
     * Max stock allowance.
     */
    private final double maxStockCount = 100;
    /**
     * Percentage in which warning starts.
     */
    private final int lowStockPercentage = 40;

    /**
     * Constructor.
     * @param partRepository required repository.
     * @param locationRepository required repository.
     */
    @Autowired
    public PartServiceImpl(final PartRepository partRepository, final LocationRepository locationRepository) {
        this.partRepository = partRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Retrieve details on all parts that fall below the low stock percentage at all locations.
     * @return list of part stock level dtos.
     */
    @Override
    public List<PartStockLevelDTO> getPartsAtLowStock() {
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
        }
        for (Location location : locations) {
            for (PartName partName : PartName.values()) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location.getLocationName());
                if (partStockLevelPercentage < lowStockPercentage) {
                    partStockLevelDTOs.add(new PartStockLevelDTO(partName.name(), location.getLocationName(), partStockLevelPercentage));
                }
            }
        }
        return partStockLevelDTOs;
    }

    /**
     * Get the stock level percentage for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level percentage for the part
     */
    private double getPartStockPercentageAtLocation(final PartName partName, final String location) {
        int partTypeCount = partRepository.countAllByLocation_LocationNameAndPartType_PartName(location, partName);
        return (partTypeCount * 100) / maxStockCount;
    }


}
