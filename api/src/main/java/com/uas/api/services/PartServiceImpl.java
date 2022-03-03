package com.uas.api.services;

import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartRepairsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.models.dtos.PartTypeFailureTimeDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import com.uas.api.repositories.PartTypeRepository;
import com.uas.api.repositories.RepairRepository;
import com.uas.api.repositories.projections.PartTypeFailureTimeProjection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    /**
     * Repository for communication between API and part type table in db.
     */
    private final PartTypeRepository partTypeRepository;
    /**
     * Repository for service for communicating with aircraft table in the db.
     */
    private final AircraftService aircraftService;

    /**
     * Repository for service for communicating with repairs table in the db.
     */
    private final RepairRepository repairRepository;

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
     * @param partTypeRepository required repository.
     * @param aircraftService required service.
     * @param repairRepository required repair repository.
     */
    @Autowired
    public PartServiceImpl(final PartRepository partRepository,
                           final LocationRepository locationRepository,
                           final PartTypeRepository partTypeRepository,
                           final AircraftService aircraftService,
                           final RepairRepository repairRepository) {
        this.partRepository = partRepository;
        this.locationRepository = locationRepository;
        this.partTypeRepository = partTypeRepository;
        this.aircraftService = aircraftService;
        this.repairRepository = repairRepository;
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
     * Retrieve stock levels for parts at given location.
     * @return list of part stock level dtos.
     */
    @Override
    public List<PartStockLevelDTO> getPartStockLevelsAtLocation(final String locationName) {
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        for (PartName partName : PartName.values()) {
            double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, locationName);
            partStockLevelDTOs.add(new PartStockLevelDTO(partName.name(), locationName, partStockLevelPercentage));
        }
        return partStockLevelDTOs;
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
     *  Adds a part from json data to the db.
     * @param requestData a hashmap of the json request data.
     * @return returns a string which contains errors or is blank if no errors occur.
     */
    @Override
    public String addPartFromJSON(final HashMap<String, String> requestData) {
        //stores error messages that occur in execution.
        String error = "";

        //retrieves objects from the json. Some are optional as the user input may not return an object due to error in user input.
        Optional<PartType> partType = Optional.ofNullable(partTypeRepository.findPartTypeById(Long.parseLong(requestData.get("partType"))));
        Optional<Aircraft> aircraft = aircraftService.findAircraftById(requestData.get("aircraft"));
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.get("location"));
        //string to store json manufacture datetime.
        String manufacture = requestData.get("manufacture");

        // creates enum from json string but if invalid string will set error variable.
        PartStatus partStatus = PartStatus.OPERATIONAL;
        try {
            partStatus = PartStatus.valueOf(requestData.get("partStatus"));
        } catch (Exception e) {
            error = "Invalid part status.";
        }

        //checks that valid partType and location have been entered and if not error variable set.
        if (location.isEmpty()) {
            error = "Invalid location.";
        }
        if (partType.isEmpty()) {
            error = "Invalid part type.";
        }
        //checks that the user inputted manufacture date can be formatted correctly and if not sets error.
        if (!Objects.equals(manufacture, "")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime.parse(manufacture, formatter);
            } catch (Exception e) {
                error = "Invalid datetime.";
            }
        }

        //if no errors have occured above then the parts are created and saved depending on which json inputs they have.
        if (error.equals("")) {
            if (aircraft.isPresent() && !manufacture.equals("")) {
                //part with aircraft and manufacture date
                Part part = new Part(partType.get(), aircraft.get(), location.get(), manufacture, partStatus);
                partRepository.save(part);
            } else if (!manufacture.equals("")) {
                //part without aircraft but with manufacture date
                Part part = new Part(partType.get(), location.get(), manufacture, partStatus);
                partRepository.save(part);
            } else if (aircraft.isPresent()) {
                Part part = new Part(partType.get(), aircraft.get(), location.get(), partStatus);
                partRepository.save(part);
            } else {
                //part without aircraft and without manufacture date
                Part part = new Part(partType.get(), location.get(), partStatus);
                partRepository.save(part);
            }
        }

        //returns error messages or a blank string if no error occured.
        //This is used to return a http response of ok or bad request with the error message as the body.
        return error;
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

    /**
     * Gets the failure time for all the parts in the database.
     * @return the failure time and the part name.
     */
    @Override
    public List<PartTypeFailureTimeDTO> getFailureTime() {
        List<PartTypeFailureTimeDTO> failureTime = new ArrayList<>();
        List<PartTypeFailureTimeProjection> fts = partTypeRepository.findAllProjectedBy();
        for (PartTypeFailureTimeProjection part:fts) {
            failureTime.add(new PartTypeFailureTimeDTO(part.getPartType(), part.getTypicalFailureTime()));
        }
        return failureTime;
    }

    /**
     * Get the top N most common failing parts.
     * @param topN the number of results to return.
     * @return the PartRepairsDTO list.
     */
    public List<PartRepairsDTO> getMostCommonFailingParts(final int topN) {
        Page<Map<Object, Object>> objects = repairRepository.findPartsWithMostRepairsAndTheirCost(PageRequest.of(0, topN, Sort.by(Sort.Direction.DESC, "repairCount")));
        List<PartRepairsDTO> partRepairsDTOs = new ArrayList<>();
        for (Map<Object, Object> objectMap : objects.getContent()) {
            long partNumber = (Long) objectMap.get("partNumber");
            long repairCount = (Long) objectMap.get("repairCount");
            BigDecimal totalCost = (BigDecimal) objectMap.get("totalCost");
            String partType = partTypeRepository.getPartTypeByPartNumber(partNumber);
            partRepairsDTOs.add(new PartRepairsDTO(partNumber, partType, repairCount, totalCost));
        }
        return partRepairsDTOs;
    }

    public List<Part> findPartsAssociatedWithAircraft(Aircraft aircraft){
        List<Part> parts;

        parts = partRepository.findAllPartsByAircraft(aircraft);

        return parts;
    }

    public void updatePartFlyTime(List<Part> parts, int flyTime) {
        for(int i=0; i<parts.size(); i++){
            Part part = parts.get(i);

            int flyTimeOld;
            if (parts.get(i).getFlyTimeHours()==null){
                flyTimeOld=0;
            } else {
                flyTimeOld = parts.get(i).getFlyTimeHours();
            }

            part.setFlyTimeHours(flyTime+flyTimeOld);

            partRepository.save(part);
        }
    }




}
