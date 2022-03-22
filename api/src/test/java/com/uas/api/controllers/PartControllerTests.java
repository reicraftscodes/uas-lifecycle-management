package com.uas.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.PartsController;
import com.uas.api.models.dtos.AddPartDTO;
import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartRepairsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PartsController.class)
public class PartControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PartService partService;

    @MockBean
    StockControlService stockControlService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;


    @WithMockUser(value = "user")
    @Test
    public void createPartWithAllParams() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "London", "2022-02-20 11:00:00", "OPERATIONAL");
        String json = objectMapper.writeValueAsString(addPartDTO);
        doNothing().when(partService).addPartFromJSON(addPartDTO);

        MvcResult mvcRes = mockMvc.perform(post("/parts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();


        assertEquals("{\"response\":\"Success\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithAircraft() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "G-001", "London", "", "OPERATIONAL");
        String json = objectMapper.writeValueAsString(addPartDTO);

        doNothing().when(partService).addPartFromJSON(addPartDTO);
        MvcResult mvcRes = mockMvc.perform(post("/parts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"response\":\"Success\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithManufacture() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "", "London", "2022-02-20 11:00:00", "OPERATIONAL");

        String json = objectMapper.writeValueAsString(addPartDTO);

        doNothing().when(partService).addPartFromJSON(addPartDTO);
        MvcResult mvcRes = mockMvc.perform(post("/parts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();


        assertEquals("{\"response\":\"Success\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithNeither() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "", "London", "", "OPERATIONAL");
        String json = objectMapper.writeValueAsString(addPartDTO);

        doNothing().when(partService).addPartFromJSON(addPartDTO);
        MvcResult mvcRes = mockMvc.perform(post("/parts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();


        assertEquals("{\"response\":\"Success\"}", mvcRes.getResponse().getContentAsString());
    }

    @Test
    public void whenLowStockIsCheckedThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/low-stock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAStockRequestIsMadeThenResponseShouldBeOK() throws Exception {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", 40.00, partTypes, quantities);
        String json = objectMapper.writeValueAsString(newStock);
        mockMvc.perform(post("/parts/stockrequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void whenStockIsCheckedAtAllLocationsThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void whenALocationsStockIsCheckedThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/location/stock")
                        .param("location", "Cardiff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void whenFailureTimeIsCheckedThenReturnListOfPartsWithFailureTime() throws Exception {
        mockMvc.perform(get("/parts/failuretime")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void whenTopFailingPartsIsCheckedThenTopFailingPartsShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/most-failing/{topN}", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void whenAPartTypeIsCheckedForBeingUnassignedThenAListOfAvailablePartsShouldBeReturned() throws Exception {
        long id = 1;
        String json = objectMapper.writeValueAsString(id);

        mockMvc.perform(post("/parts/get-by-type")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

    }



//    @Test
//    public void whenRequestMoreStockThenShouldBeTrue(){
//        MoreStockRequest mockStockRequest = new MoreStockRequest("Cardiff", 100.00, new ArrayList<>(), new ArrayList<>());
//        when(stockControlService.addMoreStock(mockStockRequest)).thenReturn(true);
//        Assertions.assertTrue(stockControlService.addMoreStock(mockStockRequest));
//    }



}


