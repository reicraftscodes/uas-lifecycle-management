package com.uas.api.services;

import com.uas.api.exceptions.InvalidDTOAttributeException;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.repositories.*;
import com.uas.api.repositories.projections.PartFailureTimeProjection;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    /**
     * Repository for communication between API and part type table in db.
     */
    private final PartTypeRepository partTypeRepository;
    /**
     * Repository for service for communicating with aircraft table in the db.
     */
    private final AircraftService aircraftService;
    /**
     * Repository for service for communicating with aircraft table in the db.
     */
    private final AircraftRepository aircraftRepository;
    /**
     * Repository for aircraft parts.
     */
    private final AircraftPartRepository aircraftPartRepository;
    /**
     * Repository for service for communicating with repairs table in the db.
     */
    private final RepairRepository repairRepository;
    /**
     * Repository for service for communicating with stock table in the db.
     */
    private final StockRepository stockRepository;

    /**
     * Repository for service for communicating with platform table in the db.
     */
    private final PlatformRepository platformRepository;

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
     * @param aircraftRepository required service.
     * @param repairRepository required repair repository.
     * @param aircraftPartRepository required aircraft part repository.
     * @param stockRepository required stock repository.
     * @param platformRepository required platform repository.
     */
    @Autowired
    public PartServiceImpl(final PartRepository partRepository,
                           final LocationRepository locationRepository,
                           final PartTypeRepository partTypeRepository,
                           final AircraftService aircraftService,
                           final RepairRepository repairRepository,
                           final StockRepository stockRepository,
                           final AircraftRepository aircraftRepository,
                           final AircraftPartRepository aircraftPartRepository,
                           final PlatformRepository platformRepository) {
        this.partRepository = partRepository;
        this.locationRepository = locationRepository;
        this.partTypeRepository = partTypeRepository;
        this.aircraftService = aircraftService;
        this.aircraftRepository = aircraftRepository;
        this.repairRepository = repairRepository;
        this.stockRepository = stockRepository;
        this.aircraftPartRepository = aircraftPartRepository;
        this.platformRepository = platformRepository;
    }

    /**
     * Retrieve stock levels for parts at all locations.
     * @return list of location part stock level dtos.
     */
    @Override
    public List<LocationStockLevelsDTO> getPartStockLevelsForAllLocations() throws NotFoundException {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        List<Part> parts = partRepository.findAll();
        if (locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
            throw new NotFoundException("No locations found!");
        }
        for (Location location : locations) {
            List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
            for (Part partName : parts) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location);
                partStockLevelDTOs.add(new PartStockLevelDTO(partName.getPartName(), location.getLocationName(), partStockLevelPercentage));
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
    public List<PartStockLevelDTO> getPartStockLevelsAtLocation(final String locationName) throws NotFoundException {
        Optional<Location> validLocation = locationRepository.findLocationByLocationName(locationName);
        List<Part> parts = partRepository.findAll();
        if (validLocation.isEmpty()) {
            throw new NotFoundException("Location not found!");
        }
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        for (Part partName : parts) {
            double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, validLocation.get());
            partStockLevelDTOs.add(new PartStockLevelDTO(partName.getPartName(), locationName, partStockLevelPercentage));
        }
        return partStockLevelDTOs;
    }


    /**
     * Retrieve details on all parts that fall below the low stock percentage at all locations.
     * @return list of part stock level dtos.
     */
    @Override
    public List<PartStockLevelDTO> getPartsAtLowStock() throws NotFoundException {
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        List<Part> parts = partRepository.findAll();
        if (locations.isEmpty()) {
            log.debug("No locations found when getting parts at low stock.");
            throw new NotFoundException("No locations found!");
        }
        for (Location location : locations) {
            for (Part partName : parts) {
                double partStockLevelPercentage = getPartStockPercentageAtLocation(partName, location);
                if (partStockLevelPercentage < lowStockPercentage) {
                    System.out.println(partName.getPartName());
                    System.out.println(location.getLocationName());
                    System.out.println(partStockLevelPercentage);
                    partStockLevelDTOs.add(new PartStockLevelDTO(partName.getPartName(), location.getLocationName(), partStockLevelPercentage));
                }
            }
        }
        return partStockLevelDTOs;
    }

    /**
     *  Adds a part from json data to the db.
     * @param requestData a hashmap of the json request data.
     */
    @Override
    public void addPartFromJSON(final AddPartDTO requestData) throws InvalidDTOAttributeException, NotFoundException {
        //retrieves objects from the json. Some are optional as the user input may not return an object due to error in user input.
        Optional<PartType> partType = partTypeRepository.findPartTypeById(requestData.getPartType());
        Aircraft aircraft = null;
        if (!requestData.getAircraft().equals("")) {
            Optional<Aircraft> aircraftOpt = aircraftRepository.findById(requestData.getAircraft());
            if (aircraftOpt.isEmpty()) {
                throw new NotFoundException("Aircraft not found!");
            } else {
                aircraft = aircraftOpt.get();
            }
        }
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.getLocationName());
        //string to store json manufacture datetime.
        String manufacture = requestData.getManufacture();

        // creates enum from json string but if invalid string will set error variable.
        PartStatus partStatus = null;
        if (!requestData.getPartStatus().equals("")) {
            try {
                partStatus = PartStatus.valueOf(requestData.getPartStatus());
            } catch (Exception e) {
                throw new InvalidDTOAttributeException("Invalid part status.");
            }
        }

        //checks that valid partType and location have been entered and if not error variable set.
        if (location.isEmpty()) {
            throw new InvalidDTOAttributeException("Invalid location.");
        }
        if (partType.isEmpty()) {
            throw new InvalidDTOAttributeException("Invalid part type.");
        }
        //checks that the user inputted manufacture date can be formatted correctly and if not sets error.
        LocalDateTime timeStamp = null;
        if (!manufacture.equals("")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                timeStamp = LocalDateTime.parse(manufacture, formatter);
            } catch (Exception e) {
                throw new InvalidDTOAttributeException("Invalid datetime.");
            }
        }
        //if no errors have occured above then the parts are created and saved depending on which json inputs they have.
        if (aircraft != null && !manufacture.equals("")) {
            //part with aircraft and manufacture date
            Part part = new Part(partType.get(), requestData.getPartName(), timeStamp, BigDecimal.valueOf(requestData.getPrice()), requestData.getWeight(), 0);
            partRepository.save(part);
            AircraftPart aircraftPart = new AircraftPart(aircraft, part, partStatus, Double.valueOf(0));
            aircraftPartRepository.save(aircraftPart);
        } else if (aircraft == null && !manufacture.equals("")) {
            //part without aircraft but with manufacture date
            Part part = new Part(partType.get(), requestData.getPartName(), timeStamp, BigDecimal.valueOf(requestData.getPrice()), requestData.getWeight(), 0);
            partRepository.save(part);
        } else if (aircraft != null && manufacture.equals("")) {
            Part part = new Part(partType.get(), requestData.getPartName(), BigDecimal.valueOf(requestData.getPrice()), requestData.getWeight(), 0);
            part.setPartNumber(22L);
            partRepository.save(part);
            AircraftPart aircraftPart = new AircraftPart(aircraft, part, partStatus, Double.valueOf(0));
            aircraftPartRepository.save(aircraftPart);
        } else {
            //part without aircraft and without manufacture date
            Part part = new Part(partType.get(), requestData.getPartName(), BigDecimal.valueOf(requestData.getPrice()), requestData.getWeight(), 0);
            partRepository.save(part);
        }
    }

    /**
     * Get the stock level percentage for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level percentage for the part at the location
     */
    private double getPartStockPercentageAtLocation(final Part partName, final Location location) {
        Long partTypeCount = getPartStockLevelAtLocation(partName, location);
        return (partTypeCount * 100) / maxStockCount;
    }

    /**
     * Get the Stock level count for a part at a location.
     * @param partName name of the part
     * @param location name of the location
     * @return the stock level count for the part at the location
     */
    private Long getPartStockLevelAtLocation(final Part partName, final Location location) {
        Optional<Stock> stock = Optional.ofNullable(stockRepository.findByPartAndLocation(partName, location));
        if (stock.isEmpty()) {
            return stockRepository.save(new Stock(partName, 0L, location)).getStockQuantity();
        }
        return stock.get().getStockQuantity();
    }

    /**
     * Gets the failure time for all the parts in the database.
     * @return the failure time and the part name.
     */
    @Override
    public List<PartTypeFailureTimeDTO> getFailureTime() {
        List<PartTypeFailureTimeDTO> failureTime = new ArrayList<>();
        List<PartFailureTimeProjection> fts = partRepository.findAllProjectedBy();
        for (PartFailureTimeProjection part:fts) {
            failureTime.add(new PartTypeFailureTimeDTO(part.getPartTypeName(), part.getTypicalFailureTime()));
        }
        return failureTime;
    }

    /**
     * Get the top N most common failing parts.
     * @param topN the number of results to return.
     * @return the PartRepairsDTO list.
     */
    public List<PartRepairsDTO> getMostCommonFailingParts(final int topN) throws NotFoundException {
        List<Map<Object, Object>> objects = repairRepository.findPartsWithMostRepairsAndTheirCostWithLimit(topN);
        if (objects.size() == 0) {
            throw new NotFoundException("No parts and repair costs were found!");
        }
        List<PartRepairsDTO> partRepairsDTOs = new ArrayList<>();
        for (Map<Object, Object> object : objects) {
            partRepairsDTOs.add(new PartRepairsDTO((Integer) object.get("partNumber"), partTypeRepository.getPartTypeByPartNumber((Integer) object.get("partNumber")), ((BigInteger) object.get("repairCount")).longValue(), (BigDecimal) object.get("totalCost")));
        }
        return partRepairsDTOs;
    }


    /**
     * Updates the fly time for parts.
     * @param parts The list of parts to update.
     * @param flyTime The flight time to be added to the parts flight time.
     */
    @Override
    public void updatePartFlyTime(final List<AircraftPart> parts, final int flyTime) {
        for (AircraftPart part : parts) {
            double flyTimeOld = 0;
            //checks that the part flighttime isnt null and if it is sets it to 0
            if (part.getFlightHours() == null) {
                flyTimeOld = flyTime;
            } else {
                flyTimeOld = part.getFlightHours();
                flyTimeOld += flyTime;
            }

            part.setFlightHours(flyTimeOld);
            aircraftPartRepository.save(part);
        }
    }
    /**
     * Updates the flight hours property for all Aircraft.
     * @param request The DTO.
     */
    @Override
    public void updateAllFlightHours(final LogFlightDTO request) throws NotFoundException, InvalidDTOAttributeException {
        Optional<Aircraft> aircraft = aircraftRepository.findById(request.getAircraft());
        if (aircraft.isEmpty()) {
            throw new NotFoundException("Aircraft not found!");
        }
        if (request.getFlyTime() < 0) {
            throw new InvalidDTOAttributeException("Fly time cannot be negative!");
        }
        List<AircraftPart> parts = aircraftPartRepository.findAircraftPartsByAircraft(aircraft.get());
        updatePartFlyTime(parts, request.getFlyTime());
        //updates the aircraft flight hours
        aircraftService.updateAircraftFlyTime(aircraft.get(), request.getFlyTime());

        aircraftService.updateUserAircraftFlyTime(aircraft.get(), request.getUserId(), request.getFlyTime());
    }

    /**
     * Gets all the parts for a specific part type that aren't assigned to an aircraft.
     * @param partType The part type being searched for.
     * @return returns a list of part numbers.
     */
    public List<String> availablePartsForParttype(final long partType) {
        return partRepository.findAllAvailableByType(partType);
    }

    /**
     * Get a list of all parts.
     * @return a list of part dtos.
     */
    @Override
    public List<PartDTO> getAllParts() throws NotFoundException {
        List<PartDTO> allPartDTOs = new ArrayList<>();
        List<Part> allParts = partRepository.findAll();
        if (allParts.isEmpty()) {
            throw new NotFoundException("Parts not found!");
        }
        for (Part part : allParts) {
            List<PartStockDTO> stockLocationsDTOs = new ArrayList<>();
            List<Stock> stockLocations = stockRepository.getAllByPart_PartNumber(part.getPartNumber());
            for (Stock stock : stockLocations) {
                stockLocationsDTOs.add(new PartStockDTO(stock.getPart().getPartNumber(), stock.getLocation().getLocationName(), stock.getStockQuantity()));
            }
            List<String> platformTypes = new ArrayList<>();
            List<Platform> platforms = platformRepository.findCompatiblePlatformTypesForPart(part.getPartNumber());
            for (Platform p : platforms) {
                platformTypes.add(p.getPlatformType().getName());
            }
            allPartDTOs.add(new PartDTO(
                    part.getPartNumber(),
                    part.getPartType().getPartName().getName(),
                    part.getPrice(),
                    part.getWeight(),
                    part.getTypicalFailureTime(),
                    stockLocationsDTOs,
                    platformTypes));
        }
        return allPartDTOs;
    }

    /**
     * Updates a parts status in the aircraft part db table.
     * @param partNumber The part number of the part.
     * @param partStatus The status that the part is being updated to.
     */
    @Override
    public void updatePartStatus(final long partNumber, final String partStatus) throws NotFoundException, InvalidDTOAttributeException {
        //Checks that part is present in db.
        Optional<Part> selectedPart = partRepository.findPartBypartNumber(partNumber);
        if (selectedPart.isEmpty()) {
            throw new NotFoundException("Part not found!");
        }

        //Checks if part is assigned to aircraft.
        Optional<AircraftPart> aircraftPart = Optional.ofNullable(aircraftPartRepository.findAircraftPartByPart_PartNumber(partNumber));
        if (aircraftPart.isEmpty()) {
            throw new InvalidDTOAttributeException("Part not assigned to aircraft!");
        }

        //Part status from string to enum.
        PartStatus ps;
        try {
            ps = PartStatus.valueOf(partStatus);
        } catch (Exception e) {
            throw new InvalidDTOAttributeException("Invalid part status!");
        }

        aircraftPart.get().setPartStatus(ps);
        aircraftPartRepository.save(aircraftPart.get());
    }

    /**
     * Updates a specified parts cost in the db.
     * @param partNumber The partID of the part having its price updated.
     * @param price The price it is being updated to.
     */
    @Override
    public void updatePartPrice(final long partNumber, final double price) throws NotFoundException {
        //Checks that part is present in db.
        Optional<Part> selectedPart = partRepository.findPartBypartNumber(partNumber);
        if (selectedPart.isEmpty()) {
            throw new NotFoundException("Part not found!");
        }
        selectedPart.get().setPrice(BigDecimal.valueOf(price));
        partRepository.save(selectedPart.get());
    }

    /**
     * Updates a specific part weight in the db.
     * @param partNumber The partID of the part having its price updated.
     * @param weight The new weight.
     */
    @Override
    public void updatePartWeight(final long partNumber, final long weight) throws NotFoundException {
        //Checks that part is present in db.
        Optional<Part> selectedPart = partRepository.findPartBypartNumber(partNumber);
        if (selectedPart.isEmpty()) {
            throw new NotFoundException("Part not found!");
        }

        selectedPart.get().setWeight((long) weight);
        partRepository.save(selectedPart.get());
    }

    /**
     * Updates the failure time of a specified part in the db.
     * @param partNumber The partID of the part having its failure time updated.
     * @param failureTime The new typical failure time.
     */
    @Override
    public void updateFailureTime(final long partNumber, final long failureTime) throws NotFoundException {
        Optional<Part> selectedPart = partRepository.findPartBypartNumber(partNumber);
        if (selectedPart.isEmpty()) {
            throw new NotFoundException("Part not found!");
        }

        selectedPart.get().setTypicalFailureTime(failureTime);
        partRepository.save(selectedPart.get());
    }

    /**
     * Gets the basic part information of a specific part.
     * @param partNumber The partID of the part being searched for.
     * @return A partInfoDTO.
     * @throws NotFoundException Throws an exception if the part is not found.
     */
    @Override
    public PartInfoDTO getPartInfo(final long partNumber) throws NotFoundException {
        Optional<Part> part = partRepository.findPartBypartNumber(partNumber);
        if (part.isEmpty()) {
            throw new NotFoundException("Part not found!");
        }
        String status;
        try {
            status = aircraftPartRepository
                    .findAircraftPartByPart_PartNumber(partNumber)
                    .getPartStatus()
                    .getLabel();
        } catch (Exception e) {
            status = "";
        }
        return new PartInfoDTO(partNumber, part.get().getPrice(), part.get().getWeight(), part.get().getTypicalFailureTime(), status);
    }
    /**
     * Transfers parts from one stock to another.
     * @param locationName The location to transfer parts from.
     * @param newLocationName The location to transfer parts to.
     * @param partName The name of the part to transfer.
     * @param quantity The number of parts to transfer.
     * @return A response entity indicating success/failure.
     */
    @Override
    public String transferPart(final String locationName, final String newLocationName, final String partName, final int quantity) {

        Optional<Location> location = locationRepository.findLocationByLocationName(locationName);
        Optional<Location> newLocation = locationRepository.findLocationByLocationName(newLocationName);

        if (location.get() == null || newLocation.get() == null) {
            return "Failure, one or both locations do not exist in the database.";
        }

        Optional<Stock> stock = Optional.ofNullable(stockRepository.findByLocationAndPart_PartName(location.get(), partName));

        if (stock.get().getStockQuantity() >= 1 && stock.get().getStockQuantity() >= quantity) {
            stock.get().setStockQuantity(stock.get().getStockQuantity() - quantity);
            stockRepository.save(stock.get());

            //If Stock with same part_partName doesn't exist in new location, create it and set = to quantity.
            Optional<Stock> newLocationStock = Optional.ofNullable(stockRepository.findByLocationAndPart_PartName(newLocation.get(), partName));

            if (newLocationStock.get() == null) {
                Stock newStock = new Stock(stock.get().getPart(), 0L, newLocation.get());
                stockRepository.save(newStock);
            } else {
                newLocationStock.get().setStockQuantity(newLocationStock.get().getStockQuantity() + quantity);
                stockRepository.save(newLocationStock.get());
            }
            return "Success.";
        }
        return "Failure, no stock to transfer.";
    }
    /**
     * Deletes parts from one stock to another.
     * @param locationName The location to delete parts from.
     * @param partName The name of the parts to delete.
     * @param quantity The number of parts to delete.
     * @return A response entity indicating success/failure.
     */
    @Override
    public String deletePart(final String locationName, final String partName, final int quantity) {
        Optional<Location> location = locationRepository.findLocationByLocationName(locationName);
        Optional<Stock> stock = Optional.ofNullable(stockRepository.findByLocationAndPart_PartName(location.get(), partName));
        if (stock.get().getStockQuantity() >= 1) {
            stock.get().setStockQuantity(stock.get().getStockQuantity() - quantity);
            stockRepository.save(stock.get());
            return "Success.";
        }
        return "Failure, no stock to delete.";
    }
}
