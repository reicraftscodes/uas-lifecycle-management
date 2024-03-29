package com.uas.api.controller;

import com.uas.api.models.dtos.*;
import com.uas.api.repositories.PartRepository;
import com.uas.api.services.AircraftService;
import com.uas.api.services.PartService;
import com.uas.api.services.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aircraft")
@CrossOrigin(origins = "https://uastest.herokuapp.com")
public class AircraftController {
    /**
     * Aircraft service used to communicate with the db about the aircraft table.
     */
    private final AircraftService aircraftService;
    /**
     * part service used for communication with the db about the part table.
     */
    private final PartService partService;
    /**
     * User service for communication between controller and DB.
     */
    private final UserService userService;
    /**
     * Repository for communication between service and Part table in DB.
     */
    private final PartRepository partRepository;

    /**
     * Constructor.
     * @param aircraftService Aircraft service for db communication.
     * @param partService
     * @param userService User service for communication between controller and DB.
     * @param partRepository communication between service and part table in DB.
     */
    @Autowired
    public AircraftController(final AircraftService aircraftService, final PartService partService, final UserService userService, final PartRepository partRepository) {
        this.aircraftService = aircraftService;
        this.partService = partService;
        this.userService = userService;
        this.partRepository = partRepository;
    }

    /**
     * Postmapping for requests to add aircraft to the database.
     * @param requestData JSON data from the request body turned into a HashMap.
     * @return A response to the request with either a confirmation or the error received.
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_LOGISTIC') ")
    ResponseEntity<?> addAircraft(@RequestBody final AircraftAddNewDTO requestData) {
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.

        String result = aircraftService.addAircraftFromJson(requestData);

        if (result == null) {
            return ResponseEntity.ok().body("{\"response\":\"Success\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"response\":\"" + result + "\"}");
        }
    }

    /**
     * Get mapping request for retrieving all aircraft assigned to a user.
     * @param userId the id of the user.
     * @return response entity with response.
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_LOGISTIC') ")
    public ResponseEntity<?> getUserAircraft(@PathVariable("id") final long userId) {
        if (!userService.userExistsById(userId)) {
            return ResponseEntity.badRequest().body("Failed to retrieve aircraft for user because user does not exist.");
        } else {
            List<AircraftUserDTO> userAircraftDTOs = aircraftService.getAircraftForUser(userId);
            return ResponseEntity.ok(userAircraftDTOs);
        }
    }

    /**
     * Post mapping used for updating the aircrafts and the parts associated with that aircrafts flight hours.
     * @param request takes json request body for the aircraft tailnumber, user Id and flytime to be logged.
     * @return returns a response with ok for no errors or a bad request with a body with the error message.
     */
    @PostMapping(value = "/log-flight", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('USER') ")
    public ResponseEntity<?> updateFlightHours(@RequestBody final LogFlightDTO request) throws NotFoundException {
        partService.updateAllFlightHours(request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    /**
     * Gets a the cumulative total repairs for each platform.
     * @param tailNumber
     * @return Response entity representing aircraftotalrepairs dto.
     */
    @GetMapping("/total-repairs/{tailNumber}")
    @PreAuthorize("hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') ")
    public ResponseEntity<AircraftTotalRepairsDTO> getTotalRepairs(@PathVariable final String tailNumber) {
        Integer aircraftRepairsCount = aircraftService.calculateTotalRepairs(tailNumber);
        AircraftTotalRepairsDTO aircraftTotalRepairsDTO = new AircraftTotalRepairsDTO(aircraftRepairsCount);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }
    /**
     * Gets the number of aircraft that need repairing.
     * @return a DTO representing the number of aircraft with at least one part with the status of 'Awaiting Repair'.
     */
    @GetMapping("/needing-repair")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC') ")
    public ResponseEntity<AircraftNeedingRepairsDTO> getNumberOfAircraftWithPartsNeedingRepair() {
        Integer aircraftNeedingRepair = aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        AircraftNeedingRepairsDTO aircraftNeedingRepairsDTO = new AircraftNeedingRepairsDTO(aircraftNeedingRepair);
        return ResponseEntity.ok(aircraftNeedingRepairsDTO);
    }
    /**
     * Gets the fly time hours of each platform.
     * @return list of integers which represent the fly time hours of each platform.
     */
    @GetMapping("/time-operational")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_LOGISTIC') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') ")
    public ResponseEntity<AircraftFlyTimeHoursDTO> getAircraftFlyTimeHours() {
        List<Integer> flyTimeHours = aircraftService.getFlyTimeHours();
        AircraftFlyTimeHoursDTO aircraftFlyTimeHoursDTO = new AircraftFlyTimeHoursDTO(flyTimeHours);

        return ResponseEntity.ok(aircraftFlyTimeHoursDTO);
    }

    /**
     * Updates the hours operational of an aircraft.
     * @param aircraftAddFlyTimeHoursDTOO request body.
     * @return response entity indicating success/failure.
     */
    @PostMapping("/time-operational")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateHoursOperational(@RequestBody final AircraftAddFlyTimeHoursDTO aircraftAddFlyTimeHoursDTOO) {
        AircraftFlyTimeHoursDTO aircraft = aircraftService.updateFlyTimeHours(aircraftAddFlyTimeHoursDTOO);
        return ResponseEntity.ok(aircraft);
    }
    /**
     * Gets the status of the platforms for the web.
     * @return response entity of the platformStatusDTO list containing DTO's which display platform status data.
     */
    @GetMapping("/platform-status")
    @PreAuthorize("hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') or hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<List<PlatformStatusDTO>> getPlatformStatusWeb() {
        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getPlatformStatus();
        return ResponseEntity.ok(platformStatusDTOList);
    }

    /**
     * Gets a filtered platform details list for the web.
     * @param aircraftFilterDTO the dto containing the filter options selected.
     * @return response entity of the filtered platformStatusDTO list containing platform status data.
     */
    @PostMapping("/platform-status/filter")
    @PreAuthorize("hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') ")
    public ResponseEntity<List<PlatformStatusDTO>> getPlatformStatusWebFiltered(@RequestBody final AircraftFilterDTO aircraftFilterDTO) {
        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getFilteredPlatformStatusList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses());
        return ResponseEntity.ok(platformStatusDTOList);
    }


    /**
     * Gets the platform status for android.
     * @return the platform status.
     */
    @GetMapping("/android/platform-status")
    @PreAuthorize("hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_CEO') or hasRole('ROLE_USER_COO') or hasRole('ROLE_USER_LOGISTIC') ")
    public ResponseEntity<PlatformStatusAndroidFullDTO> getPlatformStatusAndroid() {
        PlatformStatusAndroidFullDTO platformStatusAndroidDTOS = aircraftService.getPlatformStatusAndroid();
        return ResponseEntity.ok(platformStatusAndroidDTOS);
    }

    /**
     * Get method that returns a list of aircraft costs for parts and repairs aswell as total repair and part costs.
     * @return returns a response entity with the dto object.
     */
    @GetMapping("/ceo-aircraft-cost-full")
    @PreAuthorize("hasRole('ROLE_USER_CEO')")
    public ResponseEntity<?> getOverallRunningCost() {
        double spentOnParts = aircraftService.getAllTotalAircraftPartCost();
        double spentOnRepairs = aircraftService.getAllAircraftTotalRepairCost();

        AllAircraftCostsDTO ceoAircraftCostsDTO = new AllAircraftCostsDTO();
        ceoAircraftCostsDTO.setAircraft(aircraftService.getAircraftForCEOReturn());
        ceoAircraftCostsDTO.setTotalSpent(spentOnParts + spentOnRepairs);
        ceoAircraftCostsDTO.setTotalSpentOnParts(spentOnParts);
        ceoAircraftCostsDTO.setTotalSpentOnRepairs(spentOnRepairs);

        return ResponseEntity.ok(ceoAircraftCostsDTO);
    }

    /**
     * Get method that returns a smaller list with less information that getOverallRunningCost() to improve request performance.
     * @return returns a response entity with a dto object.
     */
    @GetMapping("ceo-aircraft-cost")
    @PreAuthorize("hasRole('ROLE_USER_CEO')")
    public ResponseEntity<?> getStreamlinedRunningCost() {
        return ResponseEntity.ok(aircraftService.getAircraftForCEOReturnMinimised());
    }
    /**
     * Get method that returns the repair cost for a particular aircraft.
     * @param id the id of the aircraft.
     * @return returns a response entity with the repair cost dto.
     */
    @GetMapping("ceo-aircraft-cost/{id}")
    @PreAuthorize("hasRole('ROLE_USER_CEO')")
    public ResponseEntity<?> getStreamlinedRunningCost(@PathVariable final String id) throws NotFoundException {
        return ResponseEntity.ok(aircraftService.getAircraftForCEOReturnMinimisedIdParam(id));
    }

    /**
     * Assigns a user to an aircraft by creating a new AircraftUser entity and saving it to db.
     * @param aircraftUserKeyDTO request body.
     * @return response entity with a dto object.
     */
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    @PostMapping("/assign-user")
    public ResponseEntity<?> assignUserToAircraft(@RequestBody final AircraftUserKeyDTO aircraftUserKeyDTO) {
        AircraftUserDTO aircraftUserDTO = aircraftService.assignUserToAircraft(aircraftUserKeyDTO);
        return ResponseEntity.ok(aircraftUserDTO);
    }

    /**
     * Get method that returns the parts and their statuses for a specific given aircraft.
     * @param tailNumber The tailnumber of the aircraft the parts are being searched for.
     * @return A response entity with the aircraft parts if ok or an error message if something went wrong.
     */
    @GetMapping(value = "aircraft-parts-status/{id}", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> getAircraftParts(@PathVariable("id") final String tailNumber) throws NotFoundException {
        return aircraftService.getAircraftParts(tailNumber);
    }

    /**
     * A post mapping that updates the status of a given aircraft.
     * @param aircraftStatusDTO A DTO with the aircraft tailnumber and status that it is being set to.
     * @return returns a response entity with an ok response or an error response with what the error was.
     */
    @PostMapping("update-aircraft-status")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updateAircraftStatus(@RequestBody final UpdateAircraftStatusDTO aircraftStatusDTO) throws NotFoundException {
        return aircraftService.updateAircraftStatus(aircraftStatusDTO);
    }

    /**
     * A post mapping for updating the part on a specific aircraft.
     * @param aircraftPartDTO A dto with the part being changed and the new part.
     * @return returns a response entity with either ok response or an error response with what the error was.
     */
    @PostMapping("update-aircraft-part")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<?> updateAircraftPart(@RequestBody final UpdateAircraftPartDTO aircraftPartDTO) throws NotFoundException {
        return aircraftService.updateAircraftPart(aircraftPartDTO);
    }

    /**
     * Gets all aircraft.
     * @return list of all aircraft.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<List<AircraftDTO>> getAllAircraft() {
        List<AircraftDTO> aircraftDTOList = aircraftService.getAllAircraft();
        return ResponseEntity.ok(aircraftDTOList);
    }

    /**
     * Gets a filtered platform details list for the web.
     * @param aircraftFilterDTO the dto containing the filter options selected.
     * @return response entity of the filtered platformStatusDTO list containing platform status data.
     */
    @PostMapping("/all/filter")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<List<AircraftDTO>> getAircraftFiltered(@RequestBody final AircraftFilterDTO aircraftFilterDTO) {
        List<AircraftDTO> aircraftDTOList = aircraftService.getFilteredAircraftList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses());
        return ResponseEntity.ok(aircraftDTOList);
    }

    /**
     * Get aircraft by tail number.
     * @param id the aircraft tail number
     * @return the aircraft dto
     * @throws NotFoundException
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER_LOGISTIC')")
    public ResponseEntity<AircraftDTO> getAircraft(@PathVariable final String id) throws NotFoundException {
        return ResponseEntity.ok(aircraftService.getAircraft(id));
    }

}
