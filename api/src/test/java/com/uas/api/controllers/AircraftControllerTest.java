package com.uas.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uas.api.controller.AircraftController;
import com.uas.api.models.auth.ERole;
import com.uas.api.models.auth.Role;
import com.uas.api.models.auth.User;
import com.uas.api.models.dtos.LogFlightDTO;
import com.uas.api.models.dtos.UserAircraftDTO;
import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.auth.RoleRepository;
import com.uas.api.repositories.auth.UserRepository;
import com.uas.api.response.JwtResponse;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.AircraftService;
import com.uas.api.services.AircraftServiceImpl;
import com.uas.api.services.PartService;
import com.uas.api.services.UserService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        LogFlightDTO logFlightDTO = new LogFlightDTO("G-001",12);

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
        LogFlightDTO logFlightDTO = new LogFlightDTO("G-001",12);

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
        LogFlightDTO logFlightDTO = new LogFlightDTO("G-001",-12);

        when(aircraftService.findAircraftById(anyString())).thenReturn(java.util.Optional.of(aircraft));
        Mockito.doNothing().when(aircraftService).updateAircraftFlyTime(aircraft, logFlightDTO.getFlyTime());
        String json = objectMapper.writeValueAsString(logFlightDTO);

        mockMvc.perform(post("/aircraft/log-flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("response: Fly time value cannot be negative!"));
    }


}
