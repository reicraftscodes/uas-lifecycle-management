package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
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

    private final PartRepository partRepository;
    private final LocationRepository locationRepository;

    // This will probably change.
    private final double maxStockCount = 100;

    private final int lowStockPercentage = 40;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, LocationRepository locationRepository) {
        this.partRepository = partRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Retrieve stock levels for parts at all locations.
     * @return list of location part stock level dtos.
     */
    @Override
    public List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations() {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        if(locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
        }
        for (Location location : locations) {
            List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
            for (PartName partName : PartName.values()) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location.getLocationName());
                partStockLevelDTOs.add(new PartStockLevelDTO(partName.name, location.getLocationName(), partStockLevelPercentage));
            }
            locationStockLevelsDTOs.add(new LocationStockLevelsDTO(location.getLocationName(), partStockLevelDTOs));
        }
        return locationStockLevelsDTOs;
    }

    /**
     * Retrieve details on all parts that fall below the low stock percentage at all locations.
     * @return list of part stock level dtos.
     */
    @Override
    public List<PartStockLevelDTO> getPartsAtLowStock() {
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        if(locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
        }
        for (Location location : locations) {
            for (PartName partName : PartName.values()) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location.getLocationName());
                if (partStockLevelPercentage < lowStockPercentage) {
                    partStockLevelDTOs.add(new PartStockLevelDTO(partName.name, location.getLocationName(), partStockLevelPercentage));
                }
            }
        }
        return partStockLevelDTOs;
    }

    /**
     * Get the stock level percentage for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level percentage for the part at the location
     */
    private double getPartStockPercentageAtLocation(PartName partName, String location) {
        int partTypeCount = getPartStockLevelAtLocation(partName, location);
        return (partTypeCount*100)/maxStockCount;
    }

    /**
     * Get the Stock level count for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level count for the part at the location
     */
    private int getPartStockLevelAtLocation(PartName partName, String location) {
        return partRepository.countAllByLocation_LocationNameAndPartType_PartName(location, partName);
    }
}
