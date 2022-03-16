package com.uas.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.AircraftController;
import com.uas.api.models.dtos.*;
import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.*;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.models.entities.*;
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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private RoleRepository roleRepository;

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private PartTypeRepository partTypeRepository;

    @MockBean
    private RepairRepository repairRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @WithMockUser(value = "user")
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
    public void RequestAircraftRepairTotals() throws Exception {
        List<Integer> results = new ArrayList<>();
        results.add(6);
        results.add(0);
        when(aircraftService.calculateTotalRepairs()).thenReturn(results);
        MvcResult mvcResult = mockMvc.perform(get("/aircraft/total-repairs")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String jsonString = mvcResult.getResponse().getContentAsString();

        assertEquals("{\"repairTotals\":[6,0]}", jsonString);
    }

    @WithMockUser(value = "user")
    @Test
    public void whenGetAllUserAircraft_Return2Aircraft() throws Exception {
        List<UserAircraftDTO> userAircraftDTOs = new ArrayList<>();
        userAircraftDTOs.add(new UserAircraftDTO("G-001", "St Athen", "Operational", "Platform A", 125, 300));
        userAircraftDTOs.add(new UserAircraftDTO("G-002", "St Athen", "Operational", "Platform B", 65, 250));
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

    @WithMockUser(value = "user")
    @Test
    public void updateFlightHours() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-001",12);

        when(aircraftService.findAircraftById(anyString())).thenReturn(java.util.Optional.of(aircraft));
        Mockito.doNothing().when(aircraftService).updateAircraftFlyTime(aircraft, logFlightDTO.getFlyTime());
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void updateFlightHoursNoAircraft() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-001",12);

        Mockito.doNothing().when(aircraftService).updateAircraftFlyTime(aircraft, logFlightDTO.getFlyTime());
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("response: Aircraft not found!"));
    }

    @WithMockUser(value = "user")
    @Test
    public void updateFlightHoursInvalidTime() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);
        LogFlightDTO logFlightDTO = new LogFlightDTO(2, "G-001",-12);

        when(aircraftService.findAircraftById(anyString())).thenReturn(java.util.Optional.of(aircraft));
        Mockito.doNothing().when(aircraftService).updateAircraftFlyTime(aircraft, logFlightDTO.getFlyTime());
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("response: Fly time value cannot be negative!"));
    }


    //This test needs looking at for the return.
    @WithMockUser(value = "user")
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
        AircraftAddHoursOperationalDTO aircraftAddHoursOperationalDTO = new AircraftAddHoursOperationalDTO("G-999", 10);
        AircraftHoursOperationalDTO result = new AircraftHoursOperationalDTO(serviceResponse);
        when(aircraftService.updateHoursOperational(aircraftAddHoursOperationalDTO)).thenReturn(result);
        String json1 = objectMapper.writeValueAsString(aircraftAddHoursOperationalDTO);
        MvcResult mockMvcResult = mockMvc.perform(post("/aircraft/time-operational")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                        .andExpect(status().isOk()).andReturn();

        String response = mockMvcResult.getResponse().getContentAsString();

        assertEquals("", response);
    }

    @WithMockUser(value = "user")
    @Test
    public void viewCEOFullAircraftCosts() throws Exception {
        List<AircraftCostsOverviewDTO> ceoAircraftCostsOverviewDTOList = new ArrayList<>();
        ceoAircraftCostsOverviewDTOList.add(new AircraftCostsOverviewDTO("G-001",1001.0,1002.0,2003.0));
        List<Aircraft> aircrafts = new ArrayList<>();
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        aircrafts.add(new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286));

        when(aircraftService.getTotalPartCostForSpecificAircraft(any())).thenReturn(1002.0);
        when(aircraftService.getTotalRepairCostForSpecificAircraft(any())).thenReturn(1001.0);
        when(aircraftService.getAllAircraft()).thenReturn(aircrafts);
        when(aircraftService.getAircraftForCEOReturnMinimised()).thenReturn(ceoAircraftCostsOverviewDTOList);

        mockMvc.perform(get("http://localhost:8080/aircraft/ceo-aircraft-cost")
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

    @WithMockUser(value = "user")
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

    @WithMockUser("user")
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
    @WithMockUser(value = "user")
    @Test
    public void AssignUserToAircraft() throws Exception {
        Location location = new Location();
        location.setLocationName("London");

        User user = new User("tim12", "logisticOne@snc.ac.uk", "ExamplePassword72-", "Tim", "Cormack", null, "logisticOne@snc.ac.uk");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);

        AircraftUserDTO aircraftUserDTO = new AircraftUserDTO(user, aircraft, 0L);
        AircraftUserKeyDTO aircraftUserKeyDTO = new AircraftUserKeyDTO(2L, "G-001");

        when(aircraftService.assignUserToAircraft(aircraftUserKeyDTO)).thenReturn(aircraftUserDTO);

        String json = "{\"userID\":\"2\",\"tailNumber\":\"G-004\"}";

        MvcResult mvcResult = mockMvc.perform(post("/aircraft/assign-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).characterEncoding("utf-8"))
                .andExpect(status().isOk()).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertEquals("", response);
    }

    @WithMockUser(value = "user")
    @Test
    public void getAircraftPartsSuccess() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);

        PartType partType = new PartType(Long.parseLong("1"),PartName.WING_A, BigDecimal.valueOf(200),Long.parseLong("50000"),Long.parseLong("600"));
        Part part = new Part(partType,aircraft,location,PartStatus.OPERATIONAL);
        List<Part> parts = new ArrayList<>();
        parts.add(part);

        AircraftPartsDTO aircraftPartsDTO = new AircraftPartsDTO();
        aircraftPartsDTO.setTailNumber(aircraft.getTailNumber());


        String json = "G-001";

        when(aircraftRepository.findById("G-001")).thenReturn(Optional.of(aircraft));
        when(partRepository.findAllPartsByAircraft(any())).thenReturn(parts);

        mockMvc.perform(get("/aircraft/aircraft-parts-status")
                        .content(json).characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

    }

    @WithMockUser(value = "user")
    @Test
    public void updateAircraftPartSuccess() throws Exception {
        Location location = new Location("St Athen","99 Street name",null,"CF620AA","Wales");
        Aircraft aircraft = new Aircraft("G-001",location, PlatformStatus.DESIGN, PlatformType.PLATFORM_A,286);

        PartType partType1 = new PartType(Long.parseLong("1"),PartName.WING_A, BigDecimal.valueOf(200),Long.parseLong("50000"),Long.parseLong("600"));
        PartType partType2 = new PartType(Long.parseLong("2"),PartName.WING_B, BigDecimal.valueOf(200),Long.parseLong("50000"),Long.parseLong("600"));
        Part currentPart = new Part(partType1,aircraft,location,PartStatus.OPERATIONAL);
        Part newPart = new Part(partType2,aircraft,location,PartStatus.OPERATIONAL);

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
}
