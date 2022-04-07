package com.uas.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.AircraftController;
import com.uas.api.exceptions.InvalidDTOAttributeException;
import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.*;
import com.uas.api.models.entities.enums.PartName;
import com.uas.api.models.entities.enums.PartStatus;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.*;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.AircraftService;
import com.uas.api.services.PartService;
import com.uas.api.services.UserService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = AircraftController.class)
public class AircraftControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AircraftService aircraftService;

    @MockBean
    private UserService userService;

    @MockBean
    private AircraftRepository aircraftRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    private PartService partService;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AircraftPartRepository aircraftPartRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private PartTypeRepository partTypeRepository;

    @MockBean
    private RepairRepository repairRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void AddAircraftWithCorrectJSON() throws Exception {
        Location location = new Location();
        location.setLocationName("London");
        locationRepository.save(location);

        String json = "{\"tailNumber\":\"G-999\",\"location\":\"London\",\"platformStatus\":\"DESIGN\",\"platformType\":\"Platform_A\"}";

        mockMvc.perform(post("/aircraft/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Success"));
    }
    @Test
    @WithMockUser(roles = "USER_CEO")
    public void RequestAircraftRepairTotals() throws Exception {
        List<Integer> results = new ArrayList<>();
        results.add(6);
        results.add(0);
        when(aircraftService.calculateTotalRepairs("G-001")).thenReturn(results.get(0));
        MvcResult mvcResult = mockMvc.perform(get("/aircraft/total-repairs/G-001")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jsonString = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"repairTotalForAircraft\":6}", jsonString);

        when(aircraftService.calculateTotalRepairs("G-002")).thenReturn(results.get(1));
        MvcResult mvcResult1 = mockMvc.perform(get("/aircraft/total-repairs/G-002")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jsonString1 = mvcResult1.getResponse().getContentAsString();

        assertEquals("{\"repairTotalForAircraft\":0}", jsonString1);
    }

    @Test
    @WithMockUser(roles = "USER_LOGISTIC")
    public void RequestAircraftNeedingRepair() throws Exception {

        when(aircraftService.getNumberOfAircraftWithPartsNeedingRepair()).thenReturn(4);
        MvcResult mvcResult = mockMvc.perform(get("/aircraft/needing-repair")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jsonString = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"aircraftNeedingRepair\":4}", jsonString);

    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenGetAllUserAircraft_Return2Aircraft() throws Exception {
        List<AircraftUserDTO> userAircraftDTOs = new ArrayList<>();
        userAircraftDTOs.add(new AircraftUserDTO("G-001", "St Athen", "Operational", "Platform A", 125, 300));
        userAircraftDTOs.add(new AircraftUserDTO("G-002", "St Athen", "Operational", "Platform B", 65, 250));
        when(userService.userExistsById(anyLong())).thenReturn(true);
        when(aircraftService.getAircraftForUser(anyLong())).thenReturn(userAircraftDTOs);

        mockMvc.perform(get("/aircraft/user/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$[0].location").value("St Athen"))
                .andExpect(jsonPath("$[0].platformStatus").value("Operational"))
                .andExpect(jsonPath("$[0].platformType").value("Platform A"))
                .andExpect(jsonPath("$[0].userAircraftFlyingHours").value(125))
                .andExpect(jsonPath("$[0].totalAircraftFlyingHours").value(300))
                .andExpect(jsonPath("$[1].tailNumber").value("G-002"));

        verify(this.aircraftService, times(1)).getAircraftForUser(anyLong());
        verify(this.userService, times(1)).userExistsById(anyLong());
        verifyNoMoreInteractions(this.aircraftService);
        verifyNoMoreInteractions(this.userService);
    }

    @WithMockUser(roles = "USER")
    @Test
    public void updateFlightHours() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-001",12);

        when(aircraftRepository.findById(anyString())).thenReturn(java.util.Optional.of(aircraft));
        Mockito.doNothing().when(aircraftService).updateAircraftFlyTime(aircraft, logFlightDTO.getFlyTime());
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void updateFlightHoursNoAircraft() throws Exception {
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-5678",12);
        doThrow(new NotFoundException("Aircraft not found!")).when(partService).updateAllFlightHours(any(LogFlightDTO.class));
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Aircraft not found!\",\"status\":\"BAD_REQUEST\"}"));
    }

    @WithMockUser(roles = "USER")
    @Test
    public void updateFlightHoursInvalidTime() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-001",-12);

        when(aircraftRepository.findById(anyString())).thenReturn(java.util.Optional.of(aircraft));
        doThrow(new InvalidDTOAttributeException("Fly time value cannot be negative!")).when(partService).updateAllFlightHours(any(LogFlightDTO.class));
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Fly time value cannot be negative!\",\"status\":\"BAD_REQUEST\"}"));
    }


    //This test needs looking at for the return.
    @WithMockUser(roles = "USER")
    @Test
    public void UpdateAircraftOperatingHours() throws Exception {
        Location location = new Location();
        location.setLocationName("London");
        locationRepository.save(location);

        String json = "{\"tailNumber\":\"G-999\",\"location\":\"London\",\"platformStatus\":\"DESIGN\",\"platformType\":\"Platform_A\", \"hoursToAdd\":\"1\"}";

        mockMvc.perform(post("/aircraft/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Success"));

        List<Integer> serviceResponse = new ArrayList<>();
        serviceResponse.add(11);
        AircraftAddFlyTimeHoursDTO aircraftAddFlyTimeHoursDTO = new AircraftAddFlyTimeHoursDTO("G-999", 10);
        AircraftFlyTimeHoursDTO result = new AircraftFlyTimeHoursDTO(serviceResponse);
        when(aircraftService.updateFlyTimeHours(aircraftAddFlyTimeHoursDTO)).thenReturn(result);
        String json1 = objectMapper.writeValueAsString(aircraftAddFlyTimeHoursDTO);
        MvcResult mockMvcResult = mockMvc.perform(post("/aircraft/time-operational")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                        .andExpect(status().isOk()).andReturn();

        String response = mockMvcResult.getResponse().getContentAsString();

        assertEquals("", response);
    }

    @WithMockUser(roles = "USER_CEO")
    @Test
    public void viewCEOFullAircraftCosts() throws Exception {
        List<AircraftCostsOverviewDTO> ceoAircraftCostsOverviewDTOList = new ArrayList<>();
        ceoAircraftCostsOverviewDTOList.add(new AircraftCostsOverviewDTO("G-001",1001.0,1002.0,2003.0));
        List<Aircraft> aircrafts = new ArrayList<>();
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        aircrafts.add(new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286));

        when(aircraftService.getTotalPartCostForSpecificAircraft(any())).thenReturn(1002.0);
        when(aircraftService.getTotalRepairCostForSpecificAircraft(any())).thenReturn(1001.0);
        when(aircraftService.getAircraftForCEOReturnMinimised()).thenReturn(ceoAircraftCostsOverviewDTOList);

        mockMvc.perform(get("/aircraft/ceo-aircraft-cost")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$[0].repairCost").value(1001.0))
                .andExpect(jsonPath("$[0].partCost").value(1002.0))
                .andExpect(jsonPath("$[0].totalCost").value(2003.0));
    }
    @WithMockUser(roles = "USER_CEO")
    @Test
    public void viewCEOFullAircraftCostsWithAircraftId() throws Exception {
       AircraftCostsOverviewDTO ceoAircraftCostsOverviewDTO = new AircraftCostsOverviewDTO("G-001",1001.0,1002.0,2003.0);
        List<Aircraft> aircrafts = new ArrayList<>();
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        aircrafts.add(new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286));

        when(aircraftService.getTotalPartCostForSpecificAircraft(any())).thenReturn(1002.0);
        when(aircraftService.getTotalRepairCostForSpecificAircraft(any())).thenReturn(1001.0);
        when(aircraftService.getAircraftForCEOReturnMinimisedIdParam(anyString())).thenReturn(ceoAircraftCostsOverviewDTO);

       mockMvc.perform(get("/aircraft/ceo-aircraft-cost/G-001")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tailNumber").value("G-001"))
                .andExpect(jsonPath("$.repairCost").value(1001.0))
                .andExpect(jsonPath("$.partCost").value(1002.0))
                .andExpect(jsonPath("$.totalCost").value(2003.0));
    }
    @WithMockUser(roles = "USER_CEO")
    @Test
    public void fullAircraftCostsWithAircraftThrowError() throws Exception {
        when(aircraftService.getAircraftForCEOReturnMinimisedIdParam(anyString())).thenThrow(new NotFoundException("Aircraft not found."));
        mockMvc.perform(get("/aircraft/ceo-aircraft-cost/G-006")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = "USER_CTO")
    @Test
    public void GetPlatformStatus() throws Exception {
        List<PlatformStatusDTO> platformStatusDTOList = new ArrayList<>();

        platformStatusDTOList.add(new PlatformStatusDTO(
                "G-001",
                PlatformType.PLATFORM_A,
                PlatformStatus.REPAIR,
                500,
                BigDecimal.valueOf(3000),
                "Cardiff",
                14,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(2000)));
        platformStatusDTOList.add(new PlatformStatusDTO(
                "G-002",
                PlatformType.PLATFORM_B,
                PlatformStatus.OPERATION,
                400,
                BigDecimal.valueOf(2800),
                "Cardiff",
                10,
                BigDecimal.valueOf(800),
                BigDecimal.valueOf(2000)));

        when(aircraftService.getPlatformStatus()).thenReturn(platformStatusDTOList);

        MvcResult mvcResult = mockMvc.perform(get("/aircraft/platform-status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$[0].platformStatus").value("Repair"))
                .andExpect(jsonPath("$[1].tailNumber").value("G-002"))
                .andExpect(jsonPath("$[1].platformStatus").value("Operational"))
                .andReturn();
    }

    @WithMockUser(roles = "USER_CEO")
    @Test
    public void whenIRequestThePlatformStatusForAndroidIShouldRecieveA200FullResponse() throws Exception {
        List<PlatformStatusAndroidDTO> mockOperational = new ArrayList<>();
        List<PlatformStatusAndroidDTO> mockBeingRepaired = new ArrayList<>();
        List<PlatformStatusAndroidDTO> mockAwaitingRepair = new ArrayList<>();
        List<PlatformStatusAndroidDTO> mockBeyondRepair = new ArrayList<>();
        PlatformStatusAndroidDTO mockAircraft = new PlatformStatusAndroidDTO("M100", PlatformStatus.REPAIR, "Cardiff", PlatformType.PLATFORM_A);
        mockOperational.add(mockAircraft);
        mockBeingRepaired.add(mockAircraft);
        mockAwaitingRepair.add(mockAircraft);
        mockBeyondRepair.add(mockAircraft);
        PlatformStatusAndroidFullDTO mockResult = new PlatformStatusAndroidFullDTO(mockOperational, mockBeingRepaired, mockAwaitingRepair, mockBeyondRepair);
        when(aircraftService.getPlatformStatusAndroid()).thenReturn(mockResult);

        mockMvc.perform(get("/aircraft/android/platform-status")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operational[0].tailNumber").value("M100"))
                .andExpect(jsonPath("$.operational[0].platformStatus").value("Repair"))
                .andExpect(jsonPath("$.operational[0].location").value("Cardiff"))
                .andExpect(jsonPath("$.operational[0].platformType").value("Platform A"));
    }

    //This test needs looking at for the return.
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void AssignUserToAircraft() throws Exception {
        Location location = new Location();
        location.setLocationName("London");

        User user = new User("tim12", "logisticOne@snc.ac.uk", "ExamplePassword72-", "Tim", "Cormack", null, "logisticOne@snc.ac.uk");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);

        AircraftUserDTO aircraftUserDTO = new AircraftUserDTO(aircraft.getTailNumber(), aircraft.getLocation().getLocationName(), aircraft.getPlatformStatus().getLabel(), aircraft.getPlatformType().getName(), 0, aircraft.getFlyTimeHours());
        AircraftUserKeyDTO aircraftUserKeyDTO = new AircraftUserKeyDTO("logisticOne@snc.ac.uk", "G-001");

        when(aircraftService.assignUserToAircraft(aircraftUserKeyDTO)).thenReturn(aircraftUserDTO);

        String json = "{\"userID\":\"2\",\"tailNumber\":\"G-004\"}";

        MvcResult mvcResult = mockMvc.perform(post("/aircraft/assign-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).characterEncoding("utf-8"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals("", response);
    }


    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void getAircraftPartsSuccess() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        PartType partType = new PartType(Long.parseLong("1"),PartName.WING_A);
        Part part = new Part(partType, "Gimble3000", BigDecimal.valueOf(200), Long.parseLong("50000"),Long.parseLong("600"));

        AircraftPart aircraftPart = new AircraftPart(aircraft, part, PartStatus.OPERATIONAL, Double.valueOf(0));
        List<AircraftPart> aircraftParts = new ArrayList<>();
        aircraftParts.add(aircraftPart);

        AircraftPartsDTO aircraftPartsDTO = new AircraftPartsDTO();
        aircraftPartsDTO.setStatus(aircraft.getPlatformStatus().getLabel());


        when(aircraftRepository.findById("G-001")).thenReturn(Optional.of(aircraft));
        when(aircraftPartRepository.findAircraftPartsByAircraft(any())).thenReturn(aircraftParts);

        mockMvc.perform(get("/aircraft/aircraft-parts-status/{id}", "G-001")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

    }


    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void updateAircraftPartSuccess() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);

        PartType partType1 = new PartType(Long.parseLong("1"),PartName.WING_A);
        PartType partType2 = new PartType(Long.parseLong("2"),PartName.WING_B);
        Part currentPart = new Part(partType1, "Mock part name", BigDecimal.valueOf(1000L), 750L, 0);
        Part newPart = new Part(partType2,"Mock part name", BigDecimal.valueOf(1000L), 750L, 0);

        UpdateAircraftPartDTO aircraftPartDTO = new UpdateAircraftPartDTO();
        aircraftPartDTO.setTailNumber("G-001");
        aircraftPartDTO.setNewPartNumber(2);

        String json = "{\"tailNumber\":\"G-001\",\"newPartNumber\":1}";

        when(partRepository.findPartBypartNumber(1)).thenReturn(Optional.of(currentPart));
        when(partRepository.findPartBypartNumber(2)).thenReturn(Optional.of(newPart));

        mockMvc.perform(post("/aircraft/update-aircraft-part")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @WithMockUser(roles = "USER_CEO")
    @Test
    public void whenFilterPlatforms_Return2PlatformDTOs() throws Exception {
        List<PlatformStatusDTO> platformStatusDTOs = new ArrayList<>();
        platformStatusDTOs.add(new PlatformStatusDTO(
                "G-001",
                PlatformType.PLATFORM_A,
                PlatformStatus.OPERATION,
                50, BigDecimal.valueOf(100),
                "Cardiff",
                5,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(50)));
        platformStatusDTOs.add(new PlatformStatusDTO(
                "G-002",
                PlatformType.PLATFORM_B,
                PlatformStatus.DESIGN,
                70, BigDecimal.valueOf(200),
                "Cardiff",
                5,
                BigDecimal.valueOf(150),
                BigDecimal.valueOf(50)));
        AircraftFilterDTO aircraftFilterDTO = new AircraftFilterDTO(Arrays.asList("Cardiff"), Arrays.asList("Operational", "Design"));

        when(aircraftService.getFilteredPlatformStatusList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses())).thenReturn(platformStatusDTOs);

        mockMvc.perform(post("/aircraft/platform-status/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(aircraftFilterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$[0].location").value("Cardiff"))
                .andExpect(jsonPath("$[0].platformStatus").value("Operational"))
                .andExpect(jsonPath("$[0].platformType").value("Platform A"))
                .andExpect(jsonPath("$[0].flyTimeHours").value(50))
                .andExpect(jsonPath("$[0].totalCost").value(100))
                .andExpect(jsonPath("$[1].tailNumber").value("G-002"))
                .andExpect(jsonPath("$[1].platformStatus").value("Design"));

        verify(this.aircraftService, times(1)).getFilteredPlatformStatusList(anyList(), anyList());
        verifyNoMoreInteractions(this.aircraftService);
    }


    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenFilterAircraft_Return2AircraftDTOs() throws Exception {
        List<AircraftDTO> aircraftDTOs = new ArrayList<>();
        aircraftDTOs.add(new AircraftDTO(
                "G-001",
                "Cardiff",
                PlatformStatus.OPERATION.getLabel(),
                PlatformType.PLATFORM_A.getName(),
                50));
        aircraftDTOs.add(new AircraftDTO(
                "G-002",
                "Cardiff",
                PlatformStatus.DESIGN.getLabel(),
                PlatformType.PLATFORM_B.getName(),
                70));
        AircraftFilterDTO aircraftFilterDTO = new AircraftFilterDTO(Arrays.asList("Cardiff"), Arrays.asList("Operational", "Design"));

        when(aircraftService.getFilteredAircraftList(aircraftFilterDTO.getLocations(), aircraftFilterDTO.getPlatformStatuses())).thenReturn(aircraftDTOs);

        mockMvc.perform(post("/aircraft/all/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsBytes(aircraftFilterDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$[0].location").value("Cardiff"))
                .andExpect(jsonPath("$[0].platformStatus").value("Operational"))
                .andExpect(jsonPath("$[0].platformType").value("Platform A"))
                .andExpect(jsonPath("$[0].flyTimeHours").value(50))
                .andExpect(jsonPath("$[1].tailNumber").value("G-002"))
                .andExpect(jsonPath("$[1].platformStatus").value("Design"));

        verify(this.aircraftService, times(1)).getFilteredAircraftList(anyList(), anyList());
        verifyNoMoreInteractions(this.aircraftService);
    }

    @WithMockUser(roles = "USER_CEO")
    @Test
    public void getOverallRunningCostSuccess() throws Exception {
        PartRepairDTO partRepairDTO = new PartRepairDTO(1,"Wing A",200);
        List<PartRepairDTO> repairs = new ArrayList<>();
        repairs.add(partRepairDTO);

        PartCostsDTO partCostsDTO = new PartCostsDTO("Wing A",500,"Operational",repairs);
        List<PartCostsDTO> parts = new ArrayList<>();
        parts.add(partCostsDTO);

        AircraftCostsDetailDTO aircraftCostsDetailDTO1 = new AircraftCostsDetailDTO("G-001",2500,5000,7500,parts);
        AircraftCostsDetailDTO aircraftCostsDetailDTO2 = new AircraftCostsDetailDTO("G-002",2500,5000,7500,parts);
        List<AircraftCostsDetailDTO> aircrafts = new ArrayList<>();
        aircrafts.add(aircraftCostsDetailDTO1);
        aircrafts.add(aircraftCostsDetailDTO2);

        when(aircraftService.getAllTotalAircraftPartCost()).thenReturn(10000.0);
        when(aircraftService.getAllAircraftTotalRepairCost()).thenReturn(5000.0);
        when(aircraftService.getAircraftForCEOReturn()).thenReturn(aircrafts);

        mockMvc.perform(get("/aircraft/ceo-aircraft-cost-full")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSpentOnRepairs").value(5000.0))
                .andExpect(jsonPath("$.totalSpentOnParts").value(10000.0))
                .andExpect(jsonPath("$.totalSpent").value(15000.0))
                .andExpect(jsonPath("$.aircraft").isArray())
                .andExpect(jsonPath("$.aircraft",hasSize(2)))
                .andExpect(jsonPath("$.aircraft[0].tailNumber").value("G-001"))
                .andExpect(jsonPath("$.aircraft[0].repairCost").value(2500.0))
                .andExpect(jsonPath("$.aircraft[0].partCost").value(5000.0))
                .andExpect(jsonPath("$.aircraft[0].totalCost").value(7500.0))
                .andExpect(jsonPath("$.aircraft[0].parts").isArray())
                .andExpect(jsonPath("$.aircraft[0].parts",hasSize(1)))
                .andExpect(jsonPath("$.aircraft[0].parts[0].partName").value("Wing A"))
                .andExpect(jsonPath("$.aircraft[0].parts[0].partCost").value(500.0))
                .andExpect(jsonPath("$.aircraft[0].parts[0].partStatus").value("Operational"))
                .andExpect(jsonPath("$.aircraft[0].parts[0].repairs").isArray())
                .andExpect(jsonPath("$.aircraft[0].parts[0].repairs",hasSize(1)))
                .andExpect(jsonPath("$.aircraft[0].parts[0].repairs[0].repairID").value(1))
                .andExpect(jsonPath("$.aircraft[0].parts[0].repairs[0].partType").value("Wing A"))
                .andExpect(jsonPath("$.aircraft[0].parts[0].repairs[0].cost").value(200))
                .andExpect(jsonPath("$.aircraft[1].tailNumber").value("G-002")) //
                .andExpect(jsonPath("$.aircraft[1].repairCost").value(2500.0))
                .andExpect(jsonPath("$.aircraft[1].partCost").value(5000.0))
                .andExpect(jsonPath("$.aircraft[1].totalCost").value(7500.0))
                .andExpect(jsonPath("$.aircraft[1].parts").isArray())
                .andExpect(jsonPath("$.aircraft[1].parts",hasSize(1)))
                .andExpect(jsonPath("$.aircraft[1].parts[0].partName").value("Wing A"))
                .andExpect(jsonPath("$.aircraft[1].parts[0].partCost").value(500.0))
                .andExpect(jsonPath("$.aircraft[1].parts[0].partStatus").value("Operational"))
                .andExpect(jsonPath("$.aircraft[1].parts[0].repairs").isArray())
                .andExpect(jsonPath("$.aircraft[1].parts[0].repairs",hasSize(1)))
                .andExpect(jsonPath("$.aircraft[1].parts[0].repairs[0].repairID").value(1))
                .andExpect(jsonPath("$.aircraft[1].parts[0].repairs[0].partType").value("Wing A"))
                .andExpect(jsonPath("$.aircraft[1].parts[0].repairs[0].cost").value(200));

    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void getAllAircraft() throws Exception {
        mockMvc.perform(get("/aircraft/all"))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void getTimeOperational() throws Exception {
        mockMvc.perform(get("/aircraft/time-operational"))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_CEO")
    @Test
    public void getCeoAircraftCostFull() throws Exception {
        mockMvc.perform(get("/aircraft/ceo-aircraft-cost-full"))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void updateAircraftStatus() throws Exception {
        UpdateAircraftStatusDTO mockAircraftStatus = new UpdateAircraftStatusDTO("M-009", "OPERATIONAL");
        String json = objectMapper.writeValueAsString(mockAircraftStatus);
        mockMvc.perform(post("/aircraft/update-aircraft-status")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}
