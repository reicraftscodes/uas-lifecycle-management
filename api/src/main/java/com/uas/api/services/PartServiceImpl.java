package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.StringToEnumConverter;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import com.uas.api.repositories.PartTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    private final PartTypeRepository partTypeRepository;
    private final AircraftRepository aircraftRepository;

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
     * @param partTypeRepository
     * @param aircraftRepository
     */
    @Autowired
    public PartServiceImpl(final PartRepository partRepository, final LocationRepository locationRepository, PartTypeRepository partTypeRepository, AircraftRepository aircraftRepository) {
        this.partRepository = partRepository;
        this.locationRepository = locationRepository;
        this.partTypeRepository = partTypeRepository;
        this.aircraftRepository = aircraftRepository;
    }

    /**
     * Retrieve stock levels for parts at all locations.
     * @return list of location part stock level dtos.
     */
    @Override
    public List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations() {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
        }
        for (Location location : locations) {
            List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
            for (PartName partName : PartName.values()) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location.getLocationName());
                partStockLevelDTOs.add(new PartStockLevelDTO(partName.getName(), location.getLocationName(), partStockLevelPercentage));
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

    @Override
    public String addPartFromJSON(HashMap<String,String> requestData){
        StringToEnumConverter stringToEnumConverter = new StringToEnumConverter();
        String error = "";

        Optional<PartType> partType = partTypeRepository.findById(Integer.parseInt(requestData.get("partType")));
        Optional<Aircraft> aircraft = aircraftRepository.findById(requestData.get("aircraft"));
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));

        PartStatus partStatus = PartStatus.OPERATIONAL;
        // creates enum from json string but if invalid string will set error variable.
        try {
            partStatus = stringToEnumConverter.stringToPartStatus(requestData.get("partStatus"));
        } catch(Exception e) {
            error = e.getMessage();
        }

        //checks that valid partType and location have been entered and if not error variable set.
        if (partType.isEmpty()) {
            error = "Invalid part type.";
        } else if (location.isEmpty()) {
            error = "Invalid location.";
        }

        if (error.equals("")){
            //part with aircraft and manufacture date

        }



        //part without aircraft but with manufacture date
        //part without aircraft and without manufacture date


        Part part = new Part(partType.get(),aircraft.get(),location.get(),partStatus);

        return "";
    }

    /**
     * Get the stock level percentage for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level percentage for the part at the location
     */
    private double getPartStockPercentageAtLocation(final PartName partName, final String location) {
        int partTypeCount = getPartStockLevelAtLocation(partName, location);
        return (partTypeCount * 100) / maxStockCount;
    }

    /**
     * Get the Stock level count for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level count for the part at the location
     */
    private int getPartStockLevelAtLocation(final PartName partName, final String location) {
        return partRepository.countAllByLocation_LocationNameAndPartType_PartName(location, partName);
    }


}
