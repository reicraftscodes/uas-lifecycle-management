package com.uas.api.services;

import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import com.uas.api.repositories.auth.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AircraftServiceTests {
    private Aircraft aircraftOne;


    @Mock
    private AircraftUserRepository aircraftUserRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private AircraftPartRepository aircraftPartRepository;

    @Mock
    private RepairRepository repairRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    @MockBean
    private AircraftServiceImpl aircraftService;

    @BeforeEach
    public void setUp() {
        aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
    }


    @Test
    public void whenPartsNeedingRepairThenListShouldNotBeEmpty() {
        Location location = new Location("London","123 London road",null,"LL12 2LL","England");

        Aircraft aircraft = new Aircraft("G-001",location,PlatformStatus.DESIGN,PlatformType.PLATFORM_A,0);
        Aircraft aircraft1 = new Aircraft("G-002",location,PlatformStatus.DESIGN,PlatformType.PLATFORM_B,0);


        PartType partType = new PartType(1L, PartName.WING_A);
        LocalDateTime ldc = LocalDateTime.now();

        List<AircraftPart> aircraftParts = new ArrayList<>();

        Part part = new Part(partType, PartName.WING_A.getName(), BigDecimal.valueOf(100),1000L,500L);
        Part part1 = new Part(partType, PartName.WING_A.getName(), BigDecimal.valueOf(100),1000L,500L);

        AircraftPart aircraftPart = new AircraftPart(aircraft, part, PartStatus.AWAITING_REPAIR, Double.valueOf(0));
        AircraftPart aircraftPart1 = new AircraftPart(aircraft1, part1, PartStatus.AWAITING_REPAIR, Double.valueOf(0));

        aircraftParts.add(aircraftPart);
        aircraftParts.add(aircraftPart1);

        when(aircraftPartRepository.findAircraftPartsByPartStatus(PartStatus.AWAITING_REPAIR)).thenReturn(aircraftParts);

        Assertions.assertDoesNotThrow(() -> {
            aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        });
        int total = aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        Assertions.assertTrue( total > 0, "Total is bigger than zero") ;
    }
    @Test
    public void whenNoPartsNeedRepairThenListShouldBeEmpty() {
        when(aircraftPartRepository.findAircraftPartsByPartStatus(PartStatus.AWAITING_REPAIR)).thenReturn(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> {
           aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        });
        int total = aircraftService.getNumberOfAircraftWithPartsNeedingRepair();
        Assertions.assertEquals(0, total);
}
    @Test
    public void givenGetAllAircraftForUser_ThenReturn2AircraftDTOs() {
        User user = new User();
        user.setId(2l);
        Aircraft aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "G-002",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_B,
                300);
        List<AircraftUser> aircraftUsers = new ArrayList<>();
        aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftOne, 120L));
        aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftTwo, 60L));

        when(aircraftUserRepository.findAllByUser_Id(anyLong())).thenReturn(aircraftUsers);

        List<AircraftUserDTO> userAircraftDTOs = aircraftService.getAircraftForUser(2);

        assertEquals("Should return 2 user aircraft dtos", 2, userAircraftDTOs.size());
        assertEquals("Should return tail number G-001", "G-001", userAircraftDTOs.get(0).getTailNumber());
        assertEquals("Should return tail number G-002", "G-002", userAircraftDTOs.get(1).getTailNumber());
    }

    @Test
    public void givenGetPlatformStatus_ThenReturn2PlatformStatusDTOs() {
        Aircraft aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "G-002",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_B,
                300);
        List<Aircraft> aircraft = new ArrayList<>();
        aircraft.add(aircraftOne);
        aircraft.add(aircraftTwo);

        when(aircraftRepository.findAll()).thenReturn(aircraft);
        when(repairRepository.findRepairsCountForAircraft(any())).thenReturn(5);
        when(repairRepository.findTotalRepairCostForAircraft(any())).thenReturn(100.0);

        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getPlatformStatus();

        assertEquals("Should return 2 platform status dtos", 2, platformStatusDTOList.size());
        assertEquals("Should return tail number G-001", "G-001", platformStatusDTOList.get(0).getTailNumber());
        assertEquals("Should return repairs count 5", 5, platformStatusDTOList.get(0).getRepairsCount());
        assertEquals("Should return tail number G-002", "G-002", platformStatusDTOList.get(1).getTailNumber());
    }

    @Test
    public void givenLogFlightHoursThenUpdateAircraftUser() throws NotFoundException {
        AircraftUser aircraftUser = new AircraftUser();
        Location location = new Location();
        location.setLocationName("London");
        Aircraft aircraft = new Aircraft("M-005",location , PlatformStatus.DESIGN, PlatformType.PLATFORM_A, 286);
        aircraftUser.setUserFlyingHours(20L);
        when(aircraftUserRepository.findByAircraftAndUserId(any(), anyLong())).thenReturn(Optional.of(aircraftUser));
        when(aircraftUserRepository.save(any())).thenReturn(aircraftUser);

        aircraftService.updateUserAircraftFlyTime(aircraft, 2, 5);

        assertEquals("Aircraft user should have 25 flight hours!", 25L, aircraftUser.getUserFlyingHours());
    }

    @Test
    public void givenLogFlightHoursThenThrowIllegalArgumentException() {
        AircraftUser aircraftUser = new AircraftUser();
        Location location = new Location();
        location.setLocationName("London");
        Aircraft aircraft = new Aircraft("M-005",location , PlatformStatus.DESIGN, PlatformType.PLATFORM_A, 286);
        when(aircraftUserRepository.findByAircraftAndUserId(any(Aircraft.class), anyLong())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> aircraftService.updateUserAircraftFlyTime(aircraft, 2, 5));
    }

    @Test
    public void givenAircraftThenAssignAircraftUser() {
        Location location = new Location();
        location.setLocationName("London");
        String tailNumber = "G-001";

        User user = new User("tim12", "logisticOne@snc.ac.uk", "ExamplePassword72-", "Tim", "Cormack", null, "logisticOne@snc.ac.uk");
        Aircraft aircraft = new Aircraft(tailNumber, location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A, 286);
        AircraftUserKey aircraftUserKey = new AircraftUserKey(1L, "G-001");
        AircraftUser aircraftUser = new AircraftUser(aircraftUserKey, user, aircraftOne, 0L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(aircraftRepository.findById(tailNumber)).thenReturn(Optional.of(aircraftOne));
        when(aircraftUserRepository.save(any())).thenReturn(aircraftUser);

        AircraftUserDTO aircraftUserDTO = aircraftService.assignUserToAircraft(new AircraftUserKeyDTO(1L, "G-001"));

        assertEquals("Aircraft user should have 0 flight hours!", 0L, aircraftUserDTO.getUserAircraftFlyingHours());
        assertEquals("Aircraft user DTO should have the correct User entity", aircraftOne.getPlatformStatus().getLabel(), aircraftUserDTO.getPlatformStatus());
        assertEquals("Aircraft user DTO should have the correct Aircraft tail number", aircraftOne.getTailNumber(), aircraftUserDTO.getTailNumber());
    }

    @Test
    public void whenAircraftOfAllStatusExistThenAllShouldBeReturned() {
        Aircraft aircraftOne = new Aircraft(
                "M-004",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "M-003",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.DESIGN,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftThree = new Aircraft(
                "M-002",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.REPAIR,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftFour = new Aircraft(
                "M-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.PRODUCTION,
                PlatformType.PLATFORM_A,
                250);
        List<Aircraft> repairs = new ArrayList<>();
        repairs.add(aircraftThree);
        List<Aircraft> production = new ArrayList<>();
        production.add(aircraftFour);
        List<Aircraft> design = new ArrayList<>();
        design.add(aircraftTwo);
        List<Aircraft> operational = new ArrayList<>();
        operational.add(aircraftOne);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.REPAIR)).thenReturn(repairs);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.DESIGN)).thenReturn(design);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.OPERATION)).thenReturn(operational);
        when(aircraftRepository.findAircraftsByPlatformStatus(PlatformStatus.PRODUCTION)).thenReturn(production);

        PlatformStatusAndroidFullDTO mockList = aircraftService.getPlatformStatusAndroid();
        assertEquals("Should have length of 1", 1, mockList.getOperational().size());
    }

    @Test
    public void givenGetFilteredPlatformStatus_ThenReturn2PlatformStatusDTOs() {
        Aircraft aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "G-003",
                new Location("Cardiff", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_B,
                300);
        List<Aircraft> aircraft = new ArrayList<>();
        aircraft.add(aircraftOne);
        aircraft.add(aircraftTwo);

        when(aircraftRepository.findAllByLocationsAndPlatformStatus(anyList(), anyList())).thenReturn(aircraft);
        when(repairRepository.findRepairsCountForAircraft(any())).thenReturn(5);
        when(repairRepository.findTotalRepairCostForAircraft(any())).thenReturn(100.0);

        List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getFilteredPlatformStatusList(Arrays.asList("Cardiff", "St Athen"), Arrays.asList("Operational"));

        assertEquals("Should return 2 platform status dtos", 2, platformStatusDTOList.size());
        assertEquals("Should return tail number G-001", "G-001", platformStatusDTOList.get(0).getTailNumber());
        assertEquals("Should return platform status operational", "Operational", platformStatusDTOList.get(0).getPlatformStatus());
        assertEquals("Should return tail number G-002", "G-003", platformStatusDTOList.get(1).getTailNumber());
    }

    @Test
    public void givenGetFilteredAircraft_ThenReturn2AircraftDTOs() {
        Aircraft aircraftOne = new Aircraft(
                "G-001",
                new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_A,
                250);
        Aircraft aircraftTwo = new Aircraft(
                "G-003",
                new Location("Cardiff", "address line 1", "address line 2", "CF000AA","Wales"),
                PlatformStatus.OPERATION,
                PlatformType.PLATFORM_B,
                300);
        List<Aircraft> aircraft = new ArrayList<>();
        aircraft.add(aircraftOne);
        aircraft.add(aircraftTwo);

        when(aircraftRepository.findAllByLocationsAndPlatformStatus(anyList(), anyList())).thenReturn(aircraft);

        List<AircraftDTO> aircraftDTOList = aircraftService.getFilteredAircraftList(Arrays.asList("Cardiff", "St Athen"), Arrays.asList("Operational"));

        assertEquals("Should return 2 platform status dtos", 2, aircraftDTOList.size());
        assertEquals("Should return tail number G-001", "G-001", aircraftDTOList.get(0).getTailNumber());
        assertEquals("Should return platform status operational", "Operational", aircraftDTOList.get(0).getPlatformStatus());
        assertEquals("Should return tail number G-002", "G-003", aircraftDTOList.get(1).getTailNumber());
    }

    @Test
    public void getAircraftForCeoReturnSuccess() {
        Location location = new Location("London","123 London road",null,"LL12 2LL","England");

        List<Aircraft> aircrafts = new ArrayList<>();
        Aircraft aircraft = new Aircraft("G-001",location,PlatformStatus.DESIGN,PlatformType.PLATFORM_A,0);
        Aircraft aircraft1 = new Aircraft("G-002",location,PlatformStatus.DESIGN,PlatformType.PLATFORM_B,0);
        aircrafts.add(aircraft);
        aircrafts.add(aircraft1);

        PartType partType = new PartType(1L, PartName.WING_A);

        Part part = new Part(partType, PartName.MOTOR.getName(), BigDecimal.valueOf(100),1000L,500L);
        Part part1 =  new Part(partType, PartName.WING_A.getName(), BigDecimal.valueOf(100),1000L,500L);

        AircraftPart aircraftPart = new AircraftPart(aircraft, part, PartStatus.OPERATIONAL, Double.valueOf(0));
        AircraftPart aircraftPart1 = new AircraftPart(aircraft1, part1, PartStatus.OPERATIONAL, Double.valueOf(0));
        List<AircraftPart> aircraftParts = new ArrayList<>();
        aircraftParts.add(aircraftPart);
        aircraftParts.add(aircraftPart1);

        List<Repair> repairs = new ArrayList<>();
        repairs.add(new Repair(1L,aircraftPart, BigDecimal.valueOf(100)));
        repairs.add(new Repair(2L,aircraftPart1, BigDecimal.valueOf(100)));

        when(aircraftRepository.findAll()).thenReturn(aircrafts);
        when(aircraftRepository.getTotalPartCostofAircraft(anyString())).thenReturn(1000.0);

        when(aircraftPartRepository.findAircraftPartsByAircraft(any())).thenReturn(aircraftParts);

        when(repairRepository.findAllByAircraftPart(any())).thenReturn(repairs);
        when(repairRepository.findTotalRepairCostForAircraft(any())).thenReturn(1234.0);

        List<AircraftCostsDetailDTO> costsList = aircraftService.getAircraftForCEOReturn();

        assertEquals("Expect 2 aircraft: ",2,costsList.size());
        assertEquals("Aircraft 1 should have tailNumber G-001: ","G-001",costsList.get(0).getTailNumber());
        assertEquals("Aircraft 2 should have tailNumber G-002: ","G-002",costsList.get(1).getTailNumber());
        assertEquals("Aircraft 1 should have partcost of 1000: ",1000.0,costsList.get(0).getPartCost());
        assertEquals("Aircraft 1 should have repaircost of 1234: ",1234.0,costsList.get(0).getRepairCost());
        assertEquals("Aircraft 1 should have totalcost of 2234: ",2234.0,costsList.get(0).getTotalCost());
        assertEquals("Aircraft 1 should have 2 parts: ",2,costsList.get(0).getParts().size());

}

    @Test
    public void whenAircraftIsSearchedForAndItExistsThenShouldReturnAircraft() {
        when(aircraftRepository.findById(anyString())).thenReturn(Optional.of(aircraftOne));

        Optional<Aircraft> result = aircraftRepository.findById("G-001");
        assertEquals("Should return the aircraft", result.isPresent(), result.isPresent());
        assertEquals("Should return tailnumber G-001", "G-001", result.get().getTailNumber());
    }

    @Test
    public void whenNumRepairsIsCheckedForAircraftShouldReturnInt() {
        List<Repair> mockRepairs = new ArrayList<>();
        when(repairRepository.findAllByAircraftPart_Aircraft_TailNumber("G-001")).thenReturn(mockRepairs);
        assertEquals("Should have total repairs as 0", aircraftService.calculateTotalRepairs("G-001"), 0);
    }




    @Nested
    public class AircraftServiceRequiresListTests {
        private Aircraft aircraftOne;
        private Aircraft aircraftTwo;
        private List<Aircraft> aircraft;

        @BeforeEach
        public void setUp() {
            aircraft = new ArrayList<>();
            aircraftOne = new Aircraft(
                    "G-001",
                    new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                    PlatformStatus.OPERATION,
                    PlatformType.PLATFORM_A,
                    250);
            aircraftTwo = new Aircraft(
                    "G-002",
                    new Location("St Athen", "address line 1", "address line 2", "CF000AA","Wales"),
                    PlatformStatus.OPERATION,
                    PlatformType.PLATFORM_A,
                    300);
            aircraft.add(aircraftOne);
            aircraft.add(aircraftTwo);
        }
        @Test
        public void androidCEOTest() {
            when(aircraftRepository.findAll()).thenReturn(aircraft);
            Assertions.assertDoesNotThrow(() -> {
                aircraftService.getAircraftForCEOReturnMinimised();
            });
        }
        @Test
        public void whenAircraftExistsAndRepairInfoIsFetchedThenShouldReturnWithoutError() {
            when(aircraftRepository.findById(anyString())).thenReturn(Optional.of(aircraftOne));
            Assertions.assertDoesNotThrow(() -> {
               aircraftService.getAircraftForCEOReturnMinimisedIdParam("G-001");
            });
        }
        @Test
        public void whenAircraftDoesNotExistAndRepairInfoIsFetchedThenShouldReturnError() {
            when(aircraftRepository.findById(anyString())).thenReturn(Optional.empty());
            NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
                aircraftService.getAircraftForCEOReturnMinimisedIdParam("G-001");
            });
            Assertions.assertEquals("Aircraft not found.", thrown.getMessage());
        }
        @Test
        public void whenFlytimeHoursForAnAircraftAreUpdatedThenNumberShouldIncrease() {
            int originalOne = aircraftOne.getFlyTimeHours();
            int originalTwo = aircraftTwo.getFlyTimeHours();
            aircraftService.updateAircraftFlyTime(aircraftOne, 5);
            int result1 = aircraftOne.getFlyTimeHours();
            assertTrue(result1 > originalOne);
            assertEquals("Should be 5 bigger", originalOne + 5, result1);
            aircraftService.updateAircraftFlyTime(aircraftTwo, 10);
            int result2 = aircraftTwo.getFlyTimeHours();
            assertTrue(result2 > originalTwo);
            assertEquals("Should be 10 bigger", originalTwo + 10, result2);
        }


        @Test
        public void givenGetAllAircraftForUser_ThenReturn2AircraftDTOs() {
            User user = new User();
            user.setId(2l);
            List<AircraftUser> aircraftUsers = new ArrayList<>();
            aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftOne, 120L));
            aircraftUsers.add(new AircraftUser(new AircraftUserKey(), user, aircraftTwo, 60L));

            when(aircraftUserRepository.findAllByUser_Id(anyLong())).thenReturn(aircraftUsers);

            List<AircraftUserDTO> userAircraftDTOs = aircraftService.getAircraftForUser(2);

            assertEquals("Should return 2 user aircraft dtos", 2, userAircraftDTOs.size());
            assertEquals("Should return tail number G-001", "G-001", userAircraftDTOs.get(0).getTailNumber());
            assertEquals("Should return tail number G-002", "G-002", userAircraftDTOs.get(1).getTailNumber());
        }

        @Test
        public void givenGetFilteredPlatformStatus_ThenReturn2PlatformStatusDTOs() {
            when(aircraftRepository.findAllByLocationsAndPlatformStatus(anyList(), anyList())).thenReturn(aircraft);
            when(repairRepository.findRepairsCountForAircraft(any())).thenReturn(5);
            when(repairRepository.findTotalRepairCostForAircraft(any())).thenReturn(100.0);

            List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getFilteredPlatformStatusList(Arrays.asList("Cardiff", "St Athen"), Arrays.asList("Operational"));

            assertEquals("Should return 2 platform status dtos", 2, platformStatusDTOList.size());
            assertEquals("Should return tail number G-001", "G-001", platformStatusDTOList.get(0).getTailNumber());
            assertEquals("Should return platform status operational", "Operational", platformStatusDTOList.get(0).getPlatformStatus());
            assertEquals("Should return tail number G-002", "G-002", platformStatusDTOList.get(1).getTailNumber());
        }
        @Test
        public void givenGetPlatformStatus_ThenReturn2PlatformStatusDTOs() {
            when(aircraftRepository.findAll()).thenReturn(aircraft);
            when(repairRepository.findRepairsCountForAircraft(any())).thenReturn(5);
            when(repairRepository.findTotalRepairCostForAircraft(any())).thenReturn(100.0);

            List<PlatformStatusDTO> platformStatusDTOList = aircraftService.getPlatformStatus();

            assertEquals("Should return 2 platform status dtos", 2, platformStatusDTOList.size());
            assertEquals("Should return tail number G-001", "G-001", platformStatusDTOList.get(0).getTailNumber());
            assertEquals("Should return repairs count 5", 5, platformStatusDTOList.get(0).getRepairsCount());
            assertEquals("Should return tail number G-002", "G-002", platformStatusDTOList.get(1).getTailNumber());
        }

        @Test
        public void givenGetFilteredAircraft_ThenReturn2AircraftDTOs() {
            when(aircraftRepository.findAllByLocationsAndPlatformStatus(anyList(), anyList())).thenReturn(aircraft);

            List<AircraftDTO> aircraftDTOList = aircraftService.getFilteredAircraftList(Arrays.asList("Cardiff", "St Athen"), Arrays.asList("Operational"));

            assertEquals("Should return 2 platform status dtos", 2, aircraftDTOList.size());
            assertEquals("Should return tail number G-001", "G-001", aircraftDTOList.get(0).getTailNumber());
            assertEquals("Should return platform status operational", "Operational", aircraftDTOList.get(0).getPlatformStatus());
            assertEquals("Should return tail number G-002", "G-002", aircraftDTOList.get(1).getTailNumber());
        }

        @Test
        public void whenFlyTimeHoursIsRetrievedForAllAircraftThenAListContainingTheHoursOnlyShouldBeReturned() {
            when(aircraftRepository.findAll()).thenReturn(aircraft);
            List<Integer> mockHoursOperational = aircraftService.getFlyTimeHours();

            assertEquals("List should have size of 2", 2, mockHoursOperational.size());
            assertEquals("Hours operational of aircraft 1 should be 250", 250, mockHoursOperational.get(0));
            assertEquals("Hours operational of aircraft 2 should be 300", 300, mockHoursOperational.get(1));

        }

        @Test
        public void whenAllAircraftAreRequestedThenListOf2ShouldBeReturned() {
            when(aircraftRepository.findAll()).thenReturn(aircraft);
            List<AircraftDTO> mockAircraft = aircraftService.getAllAircraft();
            assertEquals("List should have size of 2", 2, mockAircraft.size());
        }



    }
    @Nested
    public class AircraftServiceCostTests {
        @Test
        public void getAllAircraftRepairCostShouldReturn0() {
            when(repairRepository.findTotalRepairCostForAllAircraft()).thenReturn(null);
            Double totalRepairCost = aircraftService.getAllAircraftTotalRepairCost();
            Assertions.assertEquals(0.0, totalRepairCost);
        }
        @Test
        public void getAllAircraftRepairCostShouldReturn70() {
            when(repairRepository.findTotalRepairCostForAllAircraft()).thenReturn(70.0);
            Double totalRepairCost = aircraftService.getAllAircraftTotalRepairCost();
            Assertions.assertEquals(70.0, totalRepairCost);
        }

        @Test
        public void getAllAircraftPartCostShouldReturn0() {
            when(aircraftRepository.getTotalPartCostofAllAircraft()).thenReturn(null);
            Double totalRepairCost = aircraftService.getAllTotalAircraftPartCost();
            Assertions.assertEquals(0.0, totalRepairCost);
        }
        @Test
        public void getAllAircraftPartCostShouldReturn400() {
            when(aircraftRepository.getTotalPartCostofAllAircraft()).thenReturn(400.0);
            Double totalRepairCost = aircraftService.getAllTotalAircraftPartCost();
            Assertions.assertEquals(400.0, totalRepairCost);
        }
    }


}
