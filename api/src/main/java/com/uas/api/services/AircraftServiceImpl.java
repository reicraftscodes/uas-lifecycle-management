package com.uas.api.services;

import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import com.uas.api.repositories.auth.UserRepository;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * Contains methods for communication with the aircraft_part table of the db.
     */
    private final AircraftPartRepository aircraftPartRepository;
    /**
     * Contains methods for communication with the repair table of the db.
     */
    private final RepairRepository repairRepository;
    /**
     * Contains methods for communication with the parts table of the db.
     */
    private final PartRepository partRepository;
    /**
     * Contains methods for communication with the users table of the db.
     */
    private final UserRepository userRepository;
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
     * @param aircraftPartRepository
     * @param repairRepository
     * @param partRepository
     * @param userRepository
     */
    @Autowired
    public AircraftServiceImpl(final AircraftRepository aircraftRepository,
                               final LocationRepository locationRepository,
                               final AircraftUserRepository aircraftUserRepository,
                               final AircraftPartRepository aircraftPartRepository, final RepairRepository repairRepository,
                               final PartRepository partRepository, final UserRepository userRepository) {
        this.aircraftRepository = aircraftRepository;
        this.locationRepository = locationRepository;
        this.aircraftPartRepository = aircraftPartRepository;
        this.repairRepository = repairRepository;
        this.aircraftUserRepository = aircraftUserRepository;
        this.partRepository = partRepository;
        this.userRepository = userRepository;
    }


    /**
     * Takes the post request body and adds an aircraft from this.
     * @param requestData A hashmap of the json data. Enums made automatic object creation hard so a hashmap was used.
     * @return Returns a string which is null if the aircraft is successfully added but if there is an error this string is used so
     * it can be returned in the response body.
     */
    //TODO1: Refactor this and create exceptions.
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
                String msg = "Aircraft added by user. Tailnumber:" + requestData.getTailNumber()
                        + " Location:" + requestData.getLocation()
                        + " Platform Status:" + requestData.getPlatformStatus()
                        + " Platform Type:" + requestData.getPlatformType();
                LOG.info(msg);

                return msg.toString();
            } catch (Exception e) {
                //Catches any other exceptions and sets the error message to them.
                errorMessage = e.getMessage();
            }
        }
        //Returns either null meaning no errors or an error message about what went wrong.
        return errorMessage;
    }

    /**
     * For each aircraft saved in the DB, get the hours operational and add it to the list.
     * @return returns a list of hours operational for each platform.
     */
    @Override
    public List<Integer> getFlyTimeHours() {
        List<Integer> flyTimeHoursList = new ArrayList<>();
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        for (Aircraft aircraft: aircraftList
             ) {
            flyTimeHoursList.add(aircraft.getFlyTimeHours());
        }
        return flyTimeHoursList;
    }

    /**
     * Updates the number of hours an aircraft has been operational.
     * @param aircraftAddFlyTimeHoursDTO aircraft and the hours.
     * @return the updated hours.
     */
    @Override
    public AircraftFlyTimeHoursDTO updateFlyTimeHours(final AircraftAddFlyTimeHoursDTO aircraftAddFlyTimeHoursDTO) {
        Aircraft aircraft = aircraftRepository.findById(aircraftAddFlyTimeHoursDTO.getTailNumber()).get();
        Integer hoursToAdd = aircraftAddFlyTimeHoursDTO.getHoursToAdd();
        List<Integer> flyTimeHoursList = new ArrayList<>();
        if (aircraft.getFlyTimeHours() != null) {
            hoursToAdd += aircraft.getFlyTimeHours();

        }
        flyTimeHoursList.add(hoursToAdd);
        aircraft.setFlyTimeHours(hoursToAdd);
        aircraftRepository.save(aircraft);
        return new AircraftFlyTimeHoursDTO(flyTimeHoursList);

    }
    /**
     * Gets a list of platform status dto objects, which display an overall platform status of the aircraft.
     * @return the list of platform status dto objects.
     */
    @Override
    public List<PlatformStatusDTO> getPlatformStatus() {
        List<Aircraft> aircraftList = aircraftRepository.findAll();
        List<PlatformStatusDTO> platformStatusDTOList = new ArrayList<>();
        //todo - implement get parts cost method (this involves maybe changing the db and entity)
        for (Aircraft aircraft: aircraftList) {
            PlatformStatusDTO platformStatusDTO = getPlatformStatusForAircraft(aircraft);
            platformStatusDTOList.add(platformStatusDTO);
        }
        return platformStatusDTOList;
    }

    /**
     * Gets a filtered platform details list.
     * @param locations the locations to be included in the search.
     * @param platformStatuses the platform statuses to be included in the search.
     * @return a list of PlatformStatusDTOs that match the search criteria.
     */
    @Override
    public List<PlatformStatusDTO> getFilteredPlatformStatusList(final List<String> locations, final List<String> platformStatuses) {
        List<Aircraft> aircraftList = aircraftRepository.findAllByLocationsAndPlatformStatus(locations, platformStatuses);
        List<PlatformStatusDTO> platformStatusDTOList = new ArrayList<>();
        for (Aircraft aircraft: aircraftList) {
            PlatformStatusDTO platformStatusDTO = getPlatformStatusForAircraft(aircraft);
            platformStatusDTOList.add(platformStatusDTO);
        }
        return platformStatusDTOList;
    }

    /**
     * Gets a filtered aircraft list.
     * @param locations the locations to be included in the search.
     * @param platformStatuses the platform statuses to be included in the search.
     * @return a list of AircraftDTOs that match the search criteria.
     */
    @Override
    public List<AircraftDTO> getFilteredAircraftList(final List<String> locations, final List<String> platformStatuses) {
        List<Aircraft> aircraftList = aircraftRepository.findAllByLocationsAndPlatformStatus(locations, platformStatuses);
        List<AircraftDTO> aircraftDTOList = new ArrayList<>();
        for (Aircraft aircraft: aircraftList) {
            AircraftDTO aircraftDTO = new AircraftDTO(
                    aircraft.getTailNumber(),
                    aircraft.getLocation().getLocationName(),
                    aircraft.getPlatformStatus().getLabel(),
                    aircraft.getPlatformType().getName(),
                    aircraft.getFlyTimeHours());
            aircraftDTOList.add(aircraftDTO);
        }
        return aircraftDTOList;
    }

    private PlatformStatusDTO getPlatformStatusForAircraft(final Aircraft aircraft) {
        //todo - implement get parts cost method (this involves changing the db and entity)
        Integer repairsCount = repairRepoFssitory.findRepairsCountForAircraft(aircraft.getTailNumber());
        double repairsCost = getTotalRepairCostForSpecificAircraft(aircraft);
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
        return platformStatusDTO;
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
            platforms.add(new PlatformStatusAndroidDTO(aircraft.getTailNumber(), aircraft.getPlatformStatus(), aircraft.getLocation().getLocationName(), aircraft.getPlatformType()));
        }
        return platforms;
    }

    /**
     * Get all aircraft assigned to a user.
     * @param userID the id of the user.
     * @return a list of UserAircraftDTOs.
     */
    public List<AircraftUserDTO> getAircraftForUser(final long userID) {
        List<AircraftUser> aircraftUsers = aircraftUserRepository.findAllByUser_Id(userID);
        List<AircraftUserDTO> userAircraftDTOs = new ArrayList<>();
        for (AircraftUser aircraftUser : aircraftUsers) {
            userAircraftDTOs.add(
                    new AircraftUserDTO(
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
     * @param tailNumber of the aircraft.
     * @return total repairs.
     */
    public Integer calculateTotalRepairs(final String tailNumber) {

        Integer totalRepairs = repairRepository.findAllByAircraftPart_Aircraft_TailNumber(tailNumber).size();

        return totalRepairs;
    }
    /**
     * Gets the parts that are awaiting repair and returns the quantity of these parts.
     * @return number of parts awaiting repair.
     */
    @Override
    public Integer getNumberOfAircraftWithPartsNeedingRepair() {
        //Get all parts which are awaiting repair.
        List<AircraftPart> aircraftParts = aircraftPartRepository.findAircraftPartsByPartStatus(PartStatus.AWAITING_REPAIR);
        List<Aircraft> aircraftList = new ArrayList<>();
        for (AircraftPart aircraftPart : aircraftParts) {

            if (aircraftList.contains(aircraftPart.getAircraft())) {
                continue;
            } else {
                aircraftList.add(aircraftPart.getAircraft());
            }
        }
            return aircraftList.size();
    }

    /**
     * Gets a list of all aircrafts in the db.
     * @return A list of aircraft objects.
     */
    public List<AircraftDTO> getAllAircraft() {
        List<Aircraft> allAircraft = aircraftRepository.findAll();
        List<AircraftDTO> aircraftDTOS = new ArrayList<>();
        for (Aircraft aircraft : allAircraft) {
            AircraftDTO aircraftDTO = new AircraftDTO(
                    aircraft.getTailNumber(),
                    aircraft.getLocation().getLocationName(),
                    aircraft.getPlatformStatus().getLabel(),
                    aircraft.getPlatformType().getName(),
                    aircraft.getFlyTimeHours());
            aircraftDTOS.add(aircraftDTO);
        }
        return aircraftDTOS;
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
        List<Aircraft> aircrafts = aircraftRepository.findAll();

        for (Aircraft aircraft : aircrafts) {
            double totalPartCost = getTotalPartCostForSpecificAircraft(aircraft);
            double totalRepairCost = getTotalRepairCostForSpecificAircraft(aircraft);

            AircraftCostsDetailDTO aircraftDTO = new AircraftCostsDetailDTO();

            aircraftDTO.setTailNumber(aircraft.getTailNumber());
            aircraftDTO.setRepairCost(totalRepairCost);
            aircraftDTO.setPartCost(totalPartCost);
            aircraftDTO.setTotalCost(totalPartCost + totalRepairCost);

            List<AircraftPart> parts = aircraftPartRepository.findAircraftPartsByAircraft(aircraft);

            List<PartCostsDTO> partsForAircraft = new ArrayList<>();
            for (AircraftPart aircraftPart : parts) {
                PartCostsDTO aircraftPartDTO = new PartCostsDTO();

                aircraftPartDTO.setPartName(aircraftPart.getPart().getPartType().getPartName().getName());
                aircraftPartDTO.setPartCost(aircraftPart.getPart().getPrice().doubleValue());
                aircraftPartDTO.setPartStatus(aircraftPart.getPartStatus().getLabel());

                //repairRepository.findAllByPart(part);
                List<Repair> repairs = repairRepository.findAllByAircraftPart(aircraftPart);

                List<PartRepairDTO> totalRepairs = new ArrayList<>();
                for (Repair repair : repairs) {
                    PartRepairDTO repairDTO = new PartRepairDTO();
                    repairDTO.setRepairID(repair.getId());
                    repairDTO.setPartType(repair.getAircraftPart().getPart().getPartType().getPartName().getName());
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
        List<Aircraft> aircrafts = aircraftRepository.findAll();

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
     * Creates another aircraft dto but with less information to reduce request time.
     * @return returns list of aircraft costs and repair costs dto.
     */
    @Override
    public AircraftCostsOverviewDTO getAircraftForCEOReturnMinimisedIdParam(final String aircraftId) throws NotFoundException {
        Optional<Aircraft> aircraft = aircraftRepository.findById(aircraftId);
        if (aircraft.isEmpty()) {
            throw new NotFoundException("Aircraft not found.");
        } else {
            double totalPartCost = getTotalPartCostForSpecificAircraft(aircraft.get());
            double totalRepairCost = getTotalRepairCostForSpecificAircraft(aircraft.get());
            AircraftCostsOverviewDTO ceoAircraftCostsOverviewDTO = new AircraftCostsOverviewDTO();
            ceoAircraftCostsOverviewDTO.setTailNumber(aircraft.get().getTailNumber());
            ceoAircraftCostsOverviewDTO.setRepairCost(totalRepairCost);
            ceoAircraftCostsOverviewDTO.setPartCost(totalPartCost);
            ceoAircraftCostsOverviewDTO.setTotalCost(totalRepairCost + totalPartCost);
            return ceoAircraftCostsOverviewDTO;
        }
    }

    /**
     * Used to update the user flytime of an aircraft in the database.
     * @param tailNumber The tail number of the aircraft that the hours are being updated for.
     * @param userId The user Id whose personal flight time is being updated.
     * @param flyTime The fly time to be added to the hours field.
     */
    public void updateUserAircraftFlyTime(final Aircraft tailNumber, final long userId, final int flyTime) throws NotFoundException {
        AircraftUser aircraftUser = null;
        Optional<AircraftUser> aircraftUserOpt = aircraftUserRepository.findByAircraftAndUserId(tailNumber, userId);
        if (aircraftUserOpt.isPresent()) {
            aircraftUser = aircraftUserOpt.get();
        } else {
            LOG.debug("Failed to update user aircraft flight time because the AircraftUser could not be found.");
            throw new NotFoundException("Aircraft user does not exist!");
        }
        long oldFlyTime = aircraftUser.getUserFlyingHours();
        aircraftUser.setUserFlyingHours(oldFlyTime + flyTime);
        aircraftUserRepository.save(aircraftUser);
    }

    /**
     * Used to update the status of a given aircraft.
     * @param aircraftStatusDTO A dto containing tail number and status to be gathered from the user.
     * @return returns a response entity for success or if there is a failure what the error is.
     */
    public ResponseEntity<?> updateAircraftStatus(final UpdateAircraftStatusDTO aircraftStatusDTO) throws NotFoundException {
        Optional<Aircraft> aircraft = aircraftRepository.findById(aircraftStatusDTO.getTailNumber());

        if (aircraft.isEmpty()) {
            throw new NotFoundException("Aircraft not found!");
        } else {

            PlatformStatus platformStatusEnum;
            try {
                String status = aircraftStatusDTO.getStatus();
                platformStatusEnum = PlatformStatus.valueOf(status);
            } catch (Exception e) {
                throw new NotFoundException("Invalid aircraft status!");
            }

            aircraft.get().setPlatformStatus(platformStatusEnum);
            aircraftRepository.save(aircraft.get());
            return ResponseEntity.ok("");
        }
    }

    /**
     * Gets the parts associated with a specific aircraft.
     * @param tailNumber The tailnumber for the aircraft/
     * @return returns a response entity with a body containing the tailnumber and a list of parts with statuses.
     */
    public ResponseEntity<?> getAircraftParts(final String tailNumber) throws NotFoundException {
        AircraftPartsDTO aircraftPartsDTO = new AircraftPartsDTO();
        Optional<Aircraft> aircraft = aircraftRepository.findById(tailNumber);

        if (aircraft.isEmpty()) {
            throw new NotFoundException("Aircraft not found!");
        } else {
            List<AircraftPart> parts = aircraftPartRepository.findAircraftPartsByAircraft(aircraft.get());

            List<List<String>> partsReturn = new ArrayList<>();
            for (AircraftPart part : parts) {
                AircraftPart aircraftPart = aircraftPartRepository.findAircraftPartByPart_PartNumber(part.getPart().getPartNumber());
                //creates a list of part number, type, and status to return.
                List<String> partInformation = new ArrayList<>();

                partInformation.add(part.getPart().getPartNumber().toString());
                partInformation.add(part.getPart().getPartType().getPartName().getName());
                partInformation.add(aircraftPart.getPartStatus().getLabel());

                partsReturn.add(partInformation);
            }

            aircraftPartsDTO.setStatus(aircraft.get().getPlatformStatus().getLabel());
            aircraftPartsDTO.setParts(partsReturn);

            return ResponseEntity.ok(aircraftPartsDTO);
        }
    }

    /**
     * Updates a specific aircraft with a new selected part.
     * @param aircraftPartDTO has a aircraft tailnumber and a new part field.
     * @return returns a response entity with an ok status or an error status with an error body.
     */
    public ResponseEntity<?> updateAircraftPart(final UpdateAircraftPartDTO aircraftPartDTO) throws NotFoundException {
        Optional<Aircraft> aircraft = aircraftRepository.findById(aircraftPartDTO.getTailNumber());
        if (aircraft.isEmpty()) {
            throw new NotFoundException("Aircraft is not found!");
        }

        Optional<Part> newPart = partRepository.findPartBypartNumber(aircraftPartDTO.getNewPartNumber());
        if (newPart.isEmpty()) {
            throw new NotFoundException("New part not found!");
        }

        List<AircraftPart> parts = aircraftPartRepository.findAircraftPartsByAircraft(aircraft.get());

        for (AircraftPart part : parts) {
            if (part.getPart().getPartType() == newPart.get().getPartType()) {
                aircraftPartRepository.save(part);
            }
        }
        Optional<AircraftPart> existanceCheck = Optional.ofNullable(aircraftPartRepository.findAircraftPartByPart_PartNumber(newPart.get().getPartNumber()));
        if (existanceCheck.isPresent()) {
            return ResponseEntity.badRequest().body("Part already assigned to aircraft");
        }

        AircraftPart aircraftPart = new AircraftPart(aircraft.get(), newPart.get(), PartStatus.OPERATIONAL, (double) 0L);
        partRepository.save(newPart.get());
        aircraftPartRepository.save(aircraftPart);


        return ResponseEntity.ok("");
    }


    /**
     * Used to assign an user to an aircraft.
     * @param aircraftUserKeyDTO The tail number of the aircraft that the hours are being updated for.
     * @return returns the AircraftUserDTO that is constructed from the saved AircraftUser.
     */
    @Override
    public AircraftUserDTO assignUserToAircraft(final AircraftUserKeyDTO aircraftUserKeyDTO) {
        User user = userRepository.findByEmail(aircraftUserKeyDTO.getEmail());
        AircraftUserKey aircraftUserKey = new AircraftUserKey(user.getId(), aircraftUserKeyDTO.getTailNumber());

        Aircraft aircraft = aircraftRepository.findById(aircraftUserKeyDTO.getTailNumber()).get();
        AircraftUser aircraftUser = new AircraftUser(aircraftUserKey, user, aircraft, 0L);
        AircraftUser savedAircraftUser = aircraftUserRepository.save(aircraftUser);
        AircraftUserDTO aircraftUserDTO = new AircraftUserDTO(aircraft.getTailNumber(), aircraft.getLocation().getLocationName(), aircraft.getPlatformStatus().getLabel(), aircraft.getPlatformType().getName(), savedAircraftUser.getUserFlyingHours(), aircraft.getFlyTimeHours());
        return aircraftUserDTO;
    }

    /**
     * Get aircraft by tail number.
     * @param tailNumber the aircraft tail number
     * @return the aircraft dto
     * @throws NotFoundException
     */
    public AircraftDTO getAircraft(final String tailNumber) throws NotFoundException {
        Optional<Aircraft> aircraftOpt = aircraftRepository.findById(tailNumber);
        if (!aircraftOpt.isPresent()) {
            throw new NotFoundException("Aircraft not found!");
        } else {
            Aircraft aircraft = aircraftOpt.get();
            return new AircraftDTO(
                    aircraft.getTailNumber(),
                    aircraft.getLocation().getLocationName(),
                    aircraft.getPlatformStatus().getLabel(),
                    aircraft.getPlatformType().getName(),
                    aircraft.getFlyTimeHours());
        }
    }
}

