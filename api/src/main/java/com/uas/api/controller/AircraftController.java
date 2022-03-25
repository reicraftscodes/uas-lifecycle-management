package com.uas.api.controller;

import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Part;
import com.uas.api.repositories.PartRepository;
import com.uas.api.services.AircraftService;
import com.uas.api.services.PartService;
import com.uas.api.services.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aircraft")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<?> updateFlightHours(@RequestBody final LogFlightDTO request) {
        // A request body example that a post would have
        //{
        //    "userId: 2,
        //    "aircraft":"G-001",
        //    "flyTime":12
        //}
        String error = null;
        //Gets the aircraft entity from the post request body
        Optional<Aircraft> aircraft = aircraftService.findAircraftById(request.getAircraft());

        //checks that an aircraft has been found from the aircraft input and if not sets the error variable.
        if (aircraft.isPresent()) {
            //gets all parts associated with the aircraft and stores them in the list.
            List<Part> parts = partRepository.findAllPartsByAircraft(aircraft.get());
            //Uses a try and catch statement to check if the user input hours is an integer.
            try {
                int hoursInput = request.getFlyTime();

                //checks the user input is positive and if not sets error variable
                if (hoursInput < 0) {
                    error = "Fly time value cannot be negative!";
                } else {
                    //updates the part flight hours for all parts associated with the aircraft.
                    partService.updatePartFlyTime(parts, hoursInput);
                    //updates the aircraft flight hours
                    aircraftService.updateAircraftFlyTime(aircraft.get(), hoursInput);

                    aircraftService.updateUserAircraftFlyTime(request.getAircraft(), request.getUserId(), request.getFlyTime());
                }
            } catch (Exception e) {
                error = "Unable to update flight time.";
            }
        } else {
            error = "Aircraft not found!";
        }

        //checks for errors and if no errors then returns and okay response
        if (error == null) {
            return ResponseEntity.ok("");
        } else {
            //if there are errors then it returns a bad request with a response of the error.
            return ResponseEntity.badRequest().body("response: " + error);
        }

    }
    /**
     * Gets a the cumulative total repairs for each platform.
     * @param tailNumber
     * @return Response entity representing aircraftotalrepairs dto.
     */
    @GetMapping("/total-repairs/{tailNumber}")
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
    public ResponseEntity<AircraftNeedingRepairsDTO> getNumberOfAircraftWithPartsNeedingRepair() {
        Integer aircraftNeedingRepair = aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        AircraftNeedingRepairsDTO aircraftNeedingRepairsDTO = new AircraftNeedingRepairsDTO(aircraftNeedingRepair);
        return ResponseEntity.ok(aircraftNeedingRepairsDTO);
    }
    /**
     * Gets the cumulative total repairs for each platform.
     * @return list of integers which represent the number of repairs done to each platform.
     */
    @GetMapping("/time-operational")
    public ResponseEntity<AircraftHoursOperationalDTO> getHoursOperational() {
        List<Integer> hoursOperational = aircraftService.getHoursOperational();
        AircraftHoursOperationalDTO aircraftTotalRepairsDTO = new AircraftHoursOperationalDTO(hoursOperational);

        return ResponseEntity.ok(aircraftTotalRepairsDTO);
    }

    /**
     * Updates the hours operational of an aircraft.
     * @param aircraftAddHoursOperationalDTO request body.
     * @return response entity indicating success/failure.
     */
    @PostMapping("/time-operational")
    public ResponseEntity<?> updateHoursOperational(@RequestBody final AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO) {
        AircraftHoursOperationalDTO aircraft = aircraftService.updateHoursOperational(aircraftAddHoursOperationalDTO);
        return ResponseEntity.ok(aircraft);
    }
    /**
     * Gets the status of the platforms for the web.
     * @return response entity of the platformStatusDTO list containing DTO's which display platform status data.
     */
    @GetMapping("/platform-status")
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
    public ResponseEntity<List<PlatformStatusDTO>> getPlatformStatusWebFiltered(@RequestBody final AircraftFilterDTO aircraftFilterDTO) {
        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getFilteredPlatformStatusList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses());
        return ResponseEntity.ok(platformStatusDTOList);
    }


    /**
     * Gets the platform status for android.
     * @return the platform status.
     */
    @GetMapping("/android/platform-status")
    public ResponseEntity<PlatformStatusAndroidFullDTO> getPlatformStatusAndroid() {
        PlatformStatusAndroidFullDTO platformStatusAndroidDTOS = aircraftService.getPlatformStatusAndroid();
        return ResponseEntity.ok(platformStatusAndroidDTOS);
    }

    /**
     * Get method that returns a list of aircraft costs for parts and repairs aswell as total repair and part costs.
     * @return returns a response entity with the dto object.
     */
    @GetMapping("/ceo-aircraft-cost-full")
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
    public ResponseEntity<?> getStreamlinedRunningCost() {
        return ResponseEntity.ok(aircraftService.getAircraftForCEOReturnMinimised());
    }
    /**
     * Get method that returns the repair cost for a particular aircraft.
     * @param id the id of the aircraft.
     * @return returns a response entity with the repair cost dto.
     */
    @GetMapping("ceo-aircraft-cost/{id}")
    public ResponseEntity<?> getStreamlinedRunningCost(@PathVariable final String id) throws NotFoundException {
        return ResponseEntity.ok(aircraftService.getAircraftForCEOReturnMinimisedIdParam(id));
    }

    /**
     * Assigns a user to an aircraft by creating a new AircraftUser entity and saving it to db.
     * @param aircraftUserKeyDTO request body.
     * @return response entity with a dto object.
     */
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
    @PostMapping(value = "aircraft-parts-status", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> getAircraftParts(@RequestBody final String tailNumber) {
        return aircraftService.getAircraftParts(tailNumber);
    }

    /**
     * A post mapping that updates the status of a given aircraft.
     * @param aircraftStatusDTO A DTO with the aircraft tailnumber and status that it is being set to.
     * @return returns a response entity with an ok response or an error response with what the error was.
     */
    @PostMapping("update-aircraft-status")
    public ResponseEntity<?> updateAircraftStatus(@RequestBody final UpdateAircraftStatusDTO aircraftStatusDTO) {
        return aircraftService.updateAircraftStatus(aircraftStatusDTO);
    }

    /**
     * A post mapping for updating the part on a specific aircraft.
     * @param aircraftPartDTO A dto with the part being changed and the new part.
     * @return returns a response entity with either ok response or an error response with what the error was.
     */
    @PostMapping("update-aircraft-part")
    public ResponseEntity<?> updateAircraftPart(@RequestBody final UpdateAircraftPartDTO aircraftPartDTO) {
        return aircraftService.updateAircraftPart(aircraftPartDTO);
    }

    /**
     * Gets all aircraft.
     * @return list of all aircraft.
     */
    @GetMapping("/all")
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
    public ResponseEntity<List<AircraftDTO>> getAircraftFiltered(@RequestBody final AircraftFilterDTO aircraftFilterDTO) {
        List<AircraftDTO> aircraftDTOList = aircraftService.getFilteredAircraftList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses());
        return ResponseEntity.ok(aircraftDTOList);
    }

}
