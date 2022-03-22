package com.uas.api.services;

import com.uas.api.exceptions.InvalidDTOAttributeException;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.PartType;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import com.uas.api.repositories.projections.PartTypeFailureTimeProjection;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.test.util.AssertionErrors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartServiceTests {

    @Mock
    private PartRepository partRepository;
    @Mock
    private AircraftRepository aircraftRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private PartTypeRepository partTypeRepository;
    @Mock
    private RepairRepository repairRepository;
    @MockBean
    @InjectMocks
    private PartServiceImpl partService;

    @Test
    public void givenGetLowStockPartsReturn22Parts() throws NotFoundException {
        List<Location> locations = new ArrayList<>();
        Location locationCardiff = new Location();
        locationCardiff.setLocationName("Cardiff");
        Location locationBristol = new Location();
        locationBristol.setLocationName("Bristol");
        locations.add(locationCardiff);
        locations.add(locationBristol);

        when(locationRepository.findAll()).thenReturn(locations);
        when(partRepository.countAllByLocation_LocationNameAndPartType_PartName(any(), any())).thenReturn(30);

        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartsAtLowStock();

        assertEquals("Should return 22 parts", 22, partStockLevelDTOs.size());
    }

    @Test
    public void givenGetLowStockPartsReturn0Parts() throws NotFoundException {
        List<Location> locations = new ArrayList<>();
        Location locationCardiff = new Location();
        locationCardiff.setLocationName("Cardiff");
        Location locationBristol = new Location();
        locationBristol.setLocationName("Bristol");
        locations.add(locationCardiff);
        locations.add(locationBristol);

        when(locationRepository.findAll()).thenReturn(locations);
        when(partRepository.countAllByLocation_LocationNameAndPartType_PartName(any(), any())).thenReturn(41);

        List<PartStockLevelDTO> partStockLevelDTOs = partService.getPartsAtLowStock();

        assertEquals("Should return 0 parts", 0, partStockLevelDTOs.size());
    }
    @Test
    public void whenThereAreNoLocationsErrorShouldBeThrownForGetPartsAtLowStock() {
        List<Location> locations = new ArrayList<>();
        when(locationRepository.findAll()).thenReturn(locations);

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            partService.getPartsAtLowStock();
        });

        Assertions.assertEquals("No locations found!", thrown.getMessage());
    }
    @Test
    public void whenThereAreNoLocationsErrorShouldBeThrown() {
        List<Location> locations = new ArrayList<>();
        when(locationRepository.findAll()).thenReturn(locations);

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            List<LocationStockLevelsDTO> partStockLevelDTOS = partService.getPartStockLevelsForAllLocations();
        });

        Assertions.assertEquals("No locations found!", thrown.getMessage());
    }

    @Test
    public void whenLocationsAreValidButEmptyStockThenLowStockAlertShouldBePlacedForAll() throws Exception {
        List<Location> locations = new ArrayList<>();
        Location locationCardiff = new Location();
        locationCardiff.setLocationName("Cardiff");
        Location locationBristol = new Location();
        locationBristol.setLocationName("Bristol");
        locations.add(locationCardiff);
        locations.add(locationBristol);
        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationStockLevelsDTO> partStock = partService.getPartStockLevelsForAllLocations();
        Assertions.assertEquals(2, partStock.size());
        // Testing to make sure the length is bigger than zero rather than matching specific size,
        // as the specific size of number of different part types can change.
        assertTrue(partStock.get(0).getPartStockLevelDTOs().size() > 0);
        assertTrue(partStock.get(1).getPartStockLevelDTOs().size() > 0);
    }
    @Test
    public void whenFailureTimeIsRequestedForAllPartsThenItShouldReturnAValidResponse() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        PartType mockPartType = new PartType(1L, PartName.WING_A, BigDecimal.valueOf(100), 40L, 550L);
        PartTypeFailureTimeProjection partTypeProjection = factory.createProjection(PartTypeFailureTimeProjection.class);
        partTypeProjection.setPartType(mockPartType.getPartName().getName());
        partTypeProjection.setTypicalFailureTime(mockPartType.getTypicalFailureTime());
        List<PartTypeFailureTimeProjection> queryResults = new ArrayList<>();
        queryResults.add(partTypeProjection);

        when(partTypeRepository.findAllProjectedBy()).thenReturn(queryResults);

        List<PartTypeFailureTimeDTO> results = partService.getFailureTime();
        Assertions.assertTrue(results.size() > 0);
        Assertions.assertEquals(550L, results.get(0).getFailureTime());
        Assertions.assertEquals(PartName.WING_A.getName(), results.get(0).getPartType());
    }

    @Test
    public void whenLocationIsValidThenShouldReturnStockLevelsForLocation() throws NotFoundException {
        Location mockLocation = new Location("Cardiff", "", "", "", "");
        when(locationRepository.findLocationByLocationName("Cardiff")).thenReturn(Optional.of(mockLocation));
        when(partRepository.countAllByLocation_LocationNameAndPartType_PartName(any(), any())).thenReturn(41);
        Assertions.assertDoesNotThrow(() -> partService.getPartStockLevelsAtLocation("Cardiff"));
        List<PartStockLevelDTO> stocks = partService.getPartStockLevelsAtLocation("Cardiff");
        assertTrue(stocks.size() > 0);
        Assertions.assertEquals(41.0, stocks.get(0).getStockLevelPercentage());
    }
    @Test
    public void whenLocationIsInvalidThenShouldReturnError() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            partService.getPartStockLevelsAtLocation("Invalid Location");
        });
    }
    @Test
    public void whenLocationIsNullThenShouldReturnError() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            partService.getPartStockLevelsAtLocation(null);
        });
    }
    @Test
    public void whenLocationIsEmptyThenShouldReturnError() {
        Assertions.assertThrows(NotFoundException.class, () -> {
            partService.getPartStockLevelsAtLocation("");
        });
    }
    @Test
    public void whenPartTypeIsAvailableThenShouldReturnNonEmptyList() {
        List<String> mockPartTypes = new ArrayList<>();
        mockPartTypes.add("19");
        mockPartTypes.add("20");
        mockPartTypes.add("21");
        when(partRepository.findAllAvailbleByType(5L)).thenReturn(mockPartTypes);
        List<String> result = partService.availablePartsForParttype(5L);
        AssertionErrors.assertTrue("Should be bigger than 0", result.size() > 0);
        AssertionErrors.assertTrue("First value should be 19", result.get(0).equals("19"));
    }
    @Test
    public void whenPartTypeAreNotAvailableThenShouldReturnEmptyList() {
        List<String> mockPartTypes = new ArrayList<>();
        when(partRepository.findAllAvailbleByType(1L)).thenReturn(mockPartTypes);
        List<String> result = partService.availablePartsForParttype(1L);
        AssertionErrors.assertTrue("Should be empty", result.isEmpty());
    }

    @Nested
    public class addPartFromJsonTests {
        private PartType mockPartType;
        private Aircraft mockAircraft;
        private Location mockLocation;

        @BeforeEach
        public void setUp() {
            mockPartType = new PartType(1L, PartName.WING_A, BigDecimal.valueOf(100), 40L, 550L);
            mockLocation = new Location("Cardiff", "", "", "", "");
            mockAircraft = new Aircraft("G-001", mockLocation, PlatformStatus.OPERATION, PlatformType.PLATFORM_A, 250, 250);
        }
        @Test
        public void invalidPartStatusShouldThrowError() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "", "2022-02-20 11:00:00", "NOT_VALID");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            InvalidDTOAttributeException thrown = Assertions.assertThrows(InvalidDTOAttributeException.class, () -> {
                partService.addPartFromJSON(addPartDTO);
            });
            Assertions.assertEquals("Invalid part status.", thrown.getMessage());
        }


        @Test
        public void emptyLocationShouldThrowError() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "", "2022-02-20 11:00:00", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(anyLong())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(mockAircraft.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName("")).thenReturn(Optional.empty());

            InvalidDTOAttributeException thrown = Assertions.assertThrows(InvalidDTOAttributeException.class, () -> {
                partService.addPartFromJSON(addPartDTO);
            });
            Assertions.assertEquals("Invalid location.", thrown.getMessage());
        }

        @Test
        public void invalidPartTypeShouldThrowError() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "", "2022-02-20 11:00:00", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.empty());
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            InvalidDTOAttributeException thrown = Assertions.assertThrows(InvalidDTOAttributeException.class, () -> {
                partService.addPartFromJSON(addPartDTO);
            });
            Assertions.assertEquals("Invalid part type.", thrown.getMessage());
        }

        @Test
        public void invalidDateFormatShouldThrowError() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "", "date", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            InvalidDTOAttributeException thrown = Assertions.assertThrows(InvalidDTOAttributeException.class, () -> {
                partService.addPartFromJSON(addPartDTO);
            });
            Assertions.assertEquals("Invalid datetime.", thrown.getMessage());
        }

        @Test
        public void whenValidAddPartThenNoErrorsThrown() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "", "2022-02-20 11:00:00", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            Assertions.assertDoesNotThrow(() -> partService.addPartFromJSON(addPartDTO));

        }
        @Test
        public void whenValidAddPartWithoutAircraftNoErrorsThrown() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "", "Dublin", "2022-02-20 11:00:00", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.empty());
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            Assertions.assertDoesNotThrow(() -> partService.addPartFromJSON(addPartDTO));

        }
        @Test
        public void whenValidAddPartWithoutManufactureThenNoErrorsThrown() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "Dublin", "", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.of(mockAircraft));
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            Assertions.assertDoesNotThrow(() -> partService.addPartFromJSON(addPartDTO));

        }
        @Test
        public void whenValidAddPartWithoutManufactureAndWithoutAircraftThenNoErrorsThrown() {
            AddPartDTO addPartDTO = new AddPartDTO(1L, "", "Dublin", "", "OPERATIONAL");
            when(partTypeRepository.findPartTypeById(addPartDTO.getPartType())).thenReturn(Optional.of(mockPartType));
            when(aircraftRepository.findById(addPartDTO.getTailNumber())).thenReturn(Optional.empty());
            when(locationRepository.findLocationByLocationName(addPartDTO.getLocationName())).thenReturn(Optional.of(mockLocation));

            Assertions.assertDoesNotThrow(() -> partService.addPartFromJSON(addPartDTO));

        }

        @Test
        public void whenValidUpdateFlyTimeThenShouldNotThrowError() {
            List<Part> mockParts = new ArrayList<>();
            Part mockPart = new Part(mockPartType, mockAircraft, mockLocation, PartStatus.OPERATIONAL);
            mockParts.add(mockPart);
            Assertions.assertDoesNotThrow(() -> partService.updatePartFlyTime(mockParts, 10));
            Assertions.assertEquals(10, mockPart.getFlyTimeHours());
        }
        @Test
        public void whenValidUpdateFlyTimeWithPreviousHoursThenShouldNotThrowError() {
            List<Part> mockParts = new ArrayList<>();
            Part mockPart = new Part(mockPartType, mockAircraft, mockLocation, PartStatus.OPERATIONAL);
            mockPart.setFlyTimeHours(10);
            mockParts.add(mockPart);
            Assertions.assertDoesNotThrow(() -> partService.updatePartFlyTime(mockParts, 10));
            Assertions.assertEquals(20, mockPart.getFlyTimeHours());
        }

    }
    @Nested
    public class topNTests {
        private List<Map<Object, Object>> resultsList;
        @BeforeEach
        public void setUp() {
            resultsList = new ArrayList<>();
            Map<Object, Object> map = new HashMap<>();
            map.put("partNumber", 1);
            map.put("repairCount", BigInteger.valueOf(10L));
            map.put("repairCost", BigDecimal.valueOf(400.0));
            resultsList.add(map);
            Map<Object, Object> map2 = new HashMap<>();
            map2.put("partNumber", 4);
            map2.put("repairCount", BigInteger.valueOf(5L));
            map2.put("repairCost", BigDecimal.valueOf(50.0));
            resultsList.add(map2);
            Map<Object, Object> map3 = new HashMap<>();
            map3.put("partNumber", 3);
            map3.put("repairCount", BigInteger.valueOf(1L));
            map3.put("repairCost", BigDecimal.valueOf(100.0));
            resultsList.add(map3);
            Map<Object, Object> map4 = new HashMap<>();
            map4.put("partNumber", 2);
            map4.put("repairCount", BigInteger.valueOf(15L));
            map4.put("repairCost", BigDecimal.valueOf(750.0));
            resultsList.add(map4);
            Map<Object, Object> map5 = new HashMap<>();
            map5.put("partNumber", 5);
            map5.put("repairCount", BigInteger.valueOf(10L));
            map5.put("repairCost", BigDecimal.valueOf(4000.0));
            resultsList.add(map5);
        }
        @Test
        public void whenTop3PartsAreRequestedShouldReturn3WithNoErrors() throws NotFoundException {
            List<Map<Object, Object>> shortenedList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                shortenedList.add(resultsList.get(i));
            }
            when(repairRepository.findPartsWithMostRepairsAndTheirCostWithLimit(3)).thenReturn(shortenedList);
            Assertions.assertDoesNotThrow(() -> {
                partService.getMostCommonFailingParts(3);
            });
            List<PartRepairsDTO> partRepairsDTOs = partService.getMostCommonFailingParts(3);
            Assertions.assertEquals(3, partRepairsDTOs.size());

        }

        @Test
        public void whenTop5PartsAreRequestedShouldReturn5WithNoErrors() throws NotFoundException {
            when(repairRepository.findPartsWithMostRepairsAndTheirCostWithLimit(5)).thenReturn(resultsList);
            Assertions.assertDoesNotThrow(() -> {
                 partService.getMostCommonFailingParts(5);
            });
            List<PartRepairsDTO> partRepairsDTOs = partService.getMostCommonFailingParts(5);
            Assertions.assertEquals(5, partRepairsDTOs.size());
        }


    }
    @Test
    public void whenTop2PartsAreRequestedAndThereAreNoPartsErrorShouldBeReturned() throws NotFoundException {
        when(repairRepository.findPartsWithMostRepairsAndTheirCostWithLimit(2)).thenReturn(new ArrayList<>());
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            partService.getMostCommonFailingParts(2);
        });
        Assertions.assertEquals("No parts and repair costs were found!", thrown.getMessage());
    }

}
