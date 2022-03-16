package com.uas.api.services;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AircraftServiceImpl implements AircraftService {
    /**
     *  Aircraft repository contains methods for communicating with aircraft table in db.
     */
    private final AircraftRepository aircraftRepository;
    /**
     * Contains methods for communication with the location table of the db.
     */
    private final LocationRepository locationRepository;
    /**
     * Contains methods for communication with the aircraft_user table of the db.
     */
    private final AircraftUserRepository aircraftUserRepository;
    /**
     * Contains methods for communication with the repair table of the db.
     */
    private final RepairRepository repairRepository;
    /**
     * Contains methods for communication with the parts table of the db.
     */
    private final PartRepository partRepository;
    /**
     * Used to output logs of what the program is doing to the console.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AircraftServiceImpl.class);
    /**
     * The error message?
     */
    private String errorMessage = null;

    /**
     * The constructor.
     * @param aircraftRepository Repository used to modify aircraft data in db.
     * @param locationRepository Repository used to retrieve location data in db.
     * @param aircraftUserRepository Repository used to modify aircraft user data in db.
     * @param repairRepository
     * @param partRepository
     */
    @Autowired
    public AircraftServiceImpl(final AircraftRepository aircraftRepository,
                               final LocationRepository locationRepository,
                               final AircraftUserRepository aircraftUserRepository,
                               final RepairRepository repairRepository,
                               final PartRepository partRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
        this.repairRepository = repairRepository;
        this.aircraftUserRepository = aircraftUserRepository;
        this.partRepository = partRepository;
    }

    /**
     * Used to save an aircraft to the database.
     * @param aircraft The aircraft entity to be saved.
     */
    public void addAircraft(final Aircraft aircraft) {
        aircraftRepository.save(aircraft);
    }

    /**
     * Takes the post request body and adds an aircraft from this.
     * @param requestData A hashmap of the json data. Enums made automatic object creation hard so a hashmap was used.
     * @return Returns a string which is null if the aircraft is successfully added but if there is an error this string is used so
     * it can be returned in the response body.
     */
    @Override
    public String addAircraftFromJson(final AircraftAddNewDTO requestData) {
        //Stores error messages and tracks if any errors have occured.
        String errorMessage = null;

        //Changes the json platform status from a string to an enum.
        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        switch (requestData.getPlatformStatus()) {
            case "Design": break;
            case "Production" :
                platformStatus = PlatformStatus.PRODUCTION;
                break;
            case "Operational" :
                platformStatus = PlatformStatus.OPERATION;
                break;
            case "Repair":
                platformStatus = PlatformStatus.REPAIR;
                break;
            default:  errorMessage = "Invalid platform status.";

        }

        //Checks that the location entered exists and creates a location object.
        Optional<Location> location = locationRepository.findLocationByLocationName(requestData.getLocation());
        if (location.isEmpty()) {
            errorMessage = "Invalid location not found.";
        }

        //Changes the json platform type to enum.
        PlatformType platformType = PlatformType.PLATFORM_A;
        switch (requestData.getPlatformType()) {
            case "Platform A" : break;
            case "Platform B" : platformType = PlatformType.PLATFORM_B; break;
            default: errorMessage = "Invalid platform type."; break;
        }

        Optional<Aircraft> aircraftCheck = aircraftRepository.findById(requestData.getTailNumber());
        if (aircraftCheck.isPresent()) {
            errorMessage = "Invalid aircraft with specified tail number already present.";
        }

        //Checks if any errors have happened and if so doesn't save the aircraft to the db.
        if (errorMessage == null) {
            Aircraft aircraft = new Aircraft(requestData.getTailNumber(), location.get(), platformStatus, platformType, 0);
            try {
                aircraftRepository.save(aircraft);
                //Logs the aircraft added to the console.
                LOG.info("Aircraft added by user. Tailnumber:" + requestData.getTailNumber()
                        + " Location:" + requestData.getLocation()
                        + " Platform Status:" + requestData.getPlatformStatus()
                        + " Platform Type:" + requestData.getPlatformType());
            } catch (Exception e) {
                //Catches any other exceptions and sets the error message to them.
                errorMessage = e.getMessage();
            }
        }
        //Returns either null meaning no errors or an error message about what went wrong.
        return errorMessage;
    }

    /**
     * Optional finds aircraft from the db by the tailnumber id.
     * @param id The tailnumber id.
     * @return returns an aircraft.
     */
    @Override
    public Optional<Aircraft> findAircraftById(final String id) {

        return (aircraftRepository.findById(id));

    }

    /**
     * For each aircraft saved in the DB, get the hours operational and add it to the list.
     * @return returns a list of hours operational for each platform.
     */
    @Override
    public List<Integer> getHoursOperational() {
        List<Integer> hoursOperationalList = new ArrayList<>();
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        for (Aircraft aircraft: aircraftList
             ) {
            hoursOperationalList.add(aircraft.getHoursOperational());
        }
        return hoursOperationalList;
    }

    /**
     * Updates the number of hours an aircraft has been operational.
     * @param aircraftAddHoursOperationalDTO aircraft and the hours.
     * @return the updated hours.
     */
    @Override
    public AircraftHoursOperationalDTO updateHoursOperational(final AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO) {
        Aircraft aircraft = aircraftRepository.findById(aircraftAddHoursOperationalDTO.getTailNumber()).get();
        Integer hoursToAdd = aircraftAddHoursOperationalDTO.getHoursToAdd();
        List<Integer> hoursOperational = new ArrayList<>();
        if (aircraft.getHoursOperational() != null) {
            hoursToAdd += aircraft.getHoursOperational();

        }
        hoursOperational.add(hoursToAdd);
        aircraft.setHoursOperational(hoursToAdd);
        aircraftRepository.save(aircraft);
        return new AircraftHoursOperationalDTO(hoursOperational);

    }
    /**
     * Gets a list of platform status dto objects, which display an overall platform status of the aircraft.
     * @return the list of platform status dto objects.
     */
    @Override
    public List<PlatformStatusDTO> getPlatformStatus() {
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        List<PlatformStatusDTO> platformStatusDTOList = new ArrayList<>();
        //todo - implement get parts cost method (this involves changing the db and entity)
        for (Aircraft aircraft: aircraftList) {
            Integer repairsCount = repairRepository.findRepairsCountForAircraft(aircraft.getTailNumber());
            Double repairsCost = getTotalRepairCostForSpecificAircraft(aircraft);
            BigDecimal partsCost = BigDecimal.valueOf(3000);
            BigDecimal totalCost = partsCost.add(BigDecimal.valueOf(repairsCost));
            PlatformStatusDTO platformStatusDTO = new PlatformStatusDTO(
                    aircraft.getTailNumber(),
                    aircraft.getPlatformType(),
                    aircraft.getPlatformStatus(),
                    aircraft.getFlyTimeHours(),
                    totalCost,
                    aircraft.getLocation().getLocationName(),
                    repairsCount,
                    BigDecimal.valueOf(repairsCost),
                    BigDecimal.valueOf(3000));
            platformStatusDTOList.add(platformStatusDTO);
        }
        return platformStatusDTOList;
    }

    /**
     * For each type of platform status, it gets the list of aircraft.
     * Adds each of these lists to the DTO and returns this.
     * @return the DTO.
     */
    @Override
    public PlatformStatusAndroidFullDTO getPlatformStatusAndroid() {
        List<PlatformStatusAndroidDTO> operational = getListOfPlatformsWithStatus(PlatformStatus.OPERATION);
        List<PlatformStatusAndroidDTO> beingRepaired = getListOfPlatformsWithStatus(PlatformStatus.PRODUCTION);
        List<PlatformStatusAndroidDTO> awaitingRepair = getListOfPlatformsWithStatus(PlatformStatus.REPAIR);
        List<PlatformStatusAndroidDTO> beyondRepair = getListOfPlatformsWithStatus(PlatformStatus.DESIGN);
        return new PlatformStatusAndroidFullDTO(operational, beingRepaired, awaitingRepair, beyondRepair);

    }

    /**
     * Takes in a platform status, finds all aircraft with this status
     * then adds details from the aircraft and adds this to a DTO list.
     * @param platformStatus
     * @return the list of platforms with this status.
     */
    private List<PlatformStatusAndroidDTO> getListOfPlatformsWithStatus(final PlatformStatus platformStatus) {
        List<PlatformStatusAndroidDTO> platforms = new ArrayList<>();
        List<Aircraft> currentAircraft = aircraftRepository.findAircraftsByPlatformStatus(platformStatus);
        for (Aircraft aircraft:currentAircraft) {
            platforms.add(new PlatformStatusAndroidDTO(aircraft.getTailNumber(), aircraft.getPlatformStatus(), aircraft.getLocation().getLocationName()));
        }
        return platforms;
    }

    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    public List<UserAircraftDTO> getAircraftForUser(final long userID) {
        List<AircraftUser> aircraftUsers = aircraftUserRepository.findAllByUser_Id(userID);
        List<UserAircraftDTO> userAircraftDTOs = new ArrayList<>();
        for (AircraftUser aircraftUser : aircraftUsers) {
            userAircraftDTOs.add(
                    new UserAircraftDTO(
                            aircraftUser.getAircraft().getTailNumber(),
                            aircraftUser.getAircraft().getLocation().getLocationName(),
                            aircraftUser.getAircraft().getPlatformStatus().getLabel(),
                            aircraftUser.getAircraft().getPlatformType().getName(),
                            aircraftUser.getUserFlyingHours(),
                            aircraftUser.getAircraft().getFlyTimeHours()));
        }
        return userAircraftDTOs;
    }

    /**
     * Used to update the flytime of an aircraft in the database.
     * @param aircraft The aircraft that the hours are being updated for.
     * @param flyTime The fly time to be added to the hours field.
     */
    public void updateAircraftFlyTime(final Aircraft aircraft, final int flyTime) {
        //the flytime currently in the database.
        int oldFlyTime = aircraft.getFlyTimeHours();

        //sets the new flytime to the new hours logged plus the old hours
        aircraft.setFlyTimeHours(oldFlyTime + flyTime);

        //saves to the db an update aircraft entity
        aircraftRepository.save(aircraft);
    }

    /**
     * Calculates the total number of repairs.
     * @return total repairs.
     */
    public List<Integer> calculateTotalRepairs() {

        Integer totalPlatA = repairRepository.findAllByPart_Aircraft_PlatformType(PlatformType.PLATFORM_A).size();
        Integer totalPlatB = repairRepository.findAllByPart_Aircraft_PlatformType(PlatformType.PLATFORM_B).size();

        List<Integer> totalRepairs = new ArrayList<>();
        totalRepairs.add(totalPlatA);
        totalRepairs.add(totalPlatB);

        return totalRepairs;
    }

    /**
     * Gets a list of all aircrafts in the db.
     * @return A list of aircraft objects.
     */
    public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    /**
     * Gets the total repair cost for all aircraft.
     * @return A double variable of the total amount spent on repairs.
     */
    public double getAllAircraftTotalRepairCost() {
        Double totalRepairCost = repairRepository.findTotalRepairCostForAllAircraft();

        if (totalRepairCost == null) {
            totalRepairCost = 0.0;
        }

        return totalRepairCost;
    }

    /**
     * Gets the total amount spent on parts for aircraft.
     * @return A double variable of amount spent on parts.
     */
    public double getAllTotalAircraftPartCost() {
        Double totalcost = aircraftRepository.getTotalPartCostofAllAircraft();

        if (totalcost == null) {
            totalcost = 0.0;
        }

        return totalcost;
    }

    /**
     * Gets the part cost for a specific aircraft.
     * @param aircraft The aircraft the part cost is being fetched for.
     * @return The total amount spent on parts for the given aircraft.
     */
    public double getTotalPartCostForSpecificAircraft(final Aircraft aircraft) {
        Double totalcost = aircraftRepository.getTotalPartCostofAircraft(aircraft.getTailNumber());

        if (totalcost == null) {
            totalcost = 0.0;
        }

        return totalcost;
    }

    /**
     * Gets the repair cost for a specific aircraft.
     * @param aircraft The aircraft that the repair cost total is being calculated for.
     * @return The total amount spent on repairs for the given aircraft.
     */
    public double getTotalRepairCostForSpecificAircraft(final Aircraft aircraft) {
        Double repairTotal = repairRepository.findTotalRepairCostForAircraft(aircraft.getTailNumber());

        if (repairTotal == null) {
            repairTotal = 0.0;
        }

        return (repairTotal);
    }

    /**
     * Creates a List of aircraft DTO objects to insert into aircraft costs cto to return to the user.
     * @return list of aircraft dto.
     */
    public List<AircraftCostsDetailDTO> getAircraftForCEOReturn() {
        List<AircraftCostsDetailDTO> ceoAircraftDTOList = new ArrayList<>();
        List<Aircraft> aircrafts = getAllAircraft();

        for (Aircraft aircraft : aircrafts) {
            double totalPartCost = getTotalPartCostForSpecificAircraft(aircraft);
            double totalRepairCost = getTotalRepairCostForSpecificAircraft(aircraft);

            AircraftCostsDetailDTO aircraftDTO = new AircraftCostsDetailDTO();

            aircraftDTO.setTailNumber(aircraft.getTailNumber());
            aircraftDTO.setRepairCost(totalRepairCost);
            aircraftDTO.setPartCost(totalPartCost);
            aircraftDTO.setTotalCost(totalPartCost + totalRepairCost);

            List<Part> parts = partRepository.findAllPartsByAircraft(aircraft);

            List<PartCostsDTO> partsForAircraft = new ArrayList<>();
            for (Part part : parts) {
                PartCostsDTO aircraftPartDTO = new PartCostsDTO();

                aircraftPartDTO.setPartName(part.getPartType().getPartName().getName());
                aircraftPartDTO.setPartCost(part.getPartType().getPrice().doubleValue());
                aircraftPartDTO.setPartStatus(part.getPartStatus().getLabel());

                List<Repair> repairs = repairRepository.findAllByPart(part);
                List<PartRepairDTO> totalRepairs = new ArrayList<>();
                for (Repair repair : repairs) {
                    PartRepairDTO repairDTO = new PartRepairDTO();
                    repairDTO.setRepairID(repair.getId());
                    repairDTO.setPartType(repair.getPart().getPartType().getPartName().getName());
                    repairDTO.setCost(repair.getCost().doubleValue());
                    totalRepairs.add(repairDTO);
                }
                aircraftPartDTO.setRepairs(totalRepairs);
                partsForAircraft.add(aircraftPartDTO);
            }
            aircraftDTO.setParts(partsForAircraft);
            ceoAircraftDTOList.add(aircraftDTO);
        }
        return ceoAircraftDTOList;
    }

    /**
     * Creates another aircraft dto but with less information to reduce request time.
     * @return returns list of aircraft costs and repair costs dto.
     */
    public List<AircraftCostsOverviewDTO> getAircraftForCEOReturnMinimised() {
        List<AircraftCostsOverviewDTO> ceoAircraftCostsOverviewDTOS = new ArrayList<>();
        List<Aircraft> aircrafts = getAllAircraft();

        for (Aircraft aircraft : aircrafts) {
            double totalPartCost = getTotalPartCostForSpecificAircraft(aircraft);
            double totalRepairCost = getTotalRepairCostForSpecificAircraft(aircraft);

            AircraftCostsOverviewDTO ceoAircraftCostsOverviewDTO = new AircraftCostsOverviewDTO();
            ceoAircraftCostsOverviewDTO.setTailNumber(aircraft.getTailNumber());
            ceoAircraftCostsOverviewDTO.setRepairCost(totalRepairCost);
            ceoAircraftCostsOverviewDTO.setPartCost(totalPartCost);
            ceoAircraftCostsOverviewDTO.setTotalCost(totalRepairCost + totalPartCost);

            ceoAircraftCostsOverviewDTOS.add(ceoAircraftCostsOverviewDTO);
        }
        return ceoAircraftCostsOverviewDTOS;
    }

    /**
     * Used to update the user flytime of an aircraft in the database.
     * @param tailNumber The tail number of the aircraft that the hours are being updated for.
     * @param userId The user Id whose personal flight time is being updated.
     * @param flyTime The fly time to be added to the hours field.
     */
    public void updateUserAircraftFlyTime(final String tailNumber, final long userId, final int flyTime) throws IllegalArgumentException {
        AircraftUser aircraftUser = null;
        Optional<AircraftUser> aircraftUserOpt = aircraftUserRepository.findByAircraft_TailNumberAndUser_Id(tailNumber, userId);
        if (aircraftUserOpt.isPresent()) {
            aircraftUser = aircraftUserOpt.get();
        } else {
            LOG.debug("Failed to update user aircraft flight time because the AircraftUser could not be found.");
            throw new IllegalArgumentException("Aircraft user does not exist!");
        }
        long oldFlyTime = aircraftUser.getUserFlyingHours();
        aircraftUser.setUserFlyingHours(oldFlyTime + flyTime);
        aircraftUserRepository.save(aircraftUser);
    }
}
