package com.uas.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.PartsController;
import com.uas.api.models.dtos.AddPartDTO;
import com.uas.api.models.dtos.PartDTO;
import com.uas.api.models.dtos.PartStockDTO;
import com.uas.api.models.dtos.StockOrderDTO;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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


    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void createPartWithAllParams() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "Mock Wing A", "Mock Location", "2022-02-20 11:00:00", 1000.0, 750L, "G-001", "OPERATIONAL");
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

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void createPartWithAircraft() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "Mock Wing A", "Mock Location", "2022-02-20 11:00:00", 1000.0, 750L, "G-001", "OPERATIONAL");
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

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void createPartWithManufacture() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "Mock Wing A", "Mock Location", "2022-02-20 11:00:00", 1000.0, 750L, "", "");

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

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void createPartWithNeither() throws Exception {
        AddPartDTO addPartDTO = new AddPartDTO(1L, "Mock Wing A", "Mock Location", "", 1000.0, 750L, "", "");
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
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenLowStockIsCheckedThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/low-stock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenAStockRequestIsMadeThenResponseShouldBeOK() throws Exception {
        ArrayList<Long> partTypes = new ArrayList<>();
        partTypes.add(1L);
        ArrayList<Integer> quantities = new ArrayList<>();
        quantities.add(1);
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
        String json = objectMapper.writeValueAsString(newStock);
        mockMvc.perform(post("/parts/stockrequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenStockIsCheckedAtAllLocationsThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/stock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenALocationsStockIsCheckedThenAListOfLowStockShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/location/stock")
                        .param("location", "Cardiff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenFailureTimeIsCheckedThenReturnListOfPartsWithFailureTime() throws Exception {
        mockMvc.perform(get("/parts/failuretime")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_CTO")
    @Test
    public void whenTopFailingPartsIsCheckedThenTopFailingPartsShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/most-failing/{topN}", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenAPartTypeIsCheckedForBeingUnassignedThenAListOfAvailablePartsShouldBeReturned() throws Exception {
        mockMvc.perform(get("/parts/get-by-type/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenGetAllParts_ReturnList() throws Exception {
        List<PartDTO> partDTOList = new ArrayList<>();

        partDTOList.add(new PartDTO(
                1,
                "Motor",
                BigDecimal.valueOf(200.00),
                500,
                750,
                Arrays.asList(new PartStockDTO(1, "Cardiff", 10)),
                Arrays.asList("Platform A")));
        partDTOList.add(new PartDTO(
                2,
                "Motor",
                BigDecimal.valueOf(250.00),
                300,
                650,
                Arrays.asList(new PartStockDTO(2, "Cardiff", 15)),
                Arrays.asList("Platform A", "Platform B")));
        ;

        when(partService.getAllParts()).thenReturn(partDTOList);

        MvcResult mvcResult = mockMvc.perform(get("/parts/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].partNumber").value(1))
                .andExpect(jsonPath("$[0].partType").value("Motor"))
                .andExpect(jsonPath("$[1].partNumber").value(2))
                .andExpect(jsonPath("$[1].compatiblePlatforms", hasSize(2)))
                .andReturn();
    }
    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void whenGetAllPartsThenThrowError() throws Exception {
        when(partService.getAllParts()).thenThrow(new NotFoundException("Parts not found."));
        mockMvc.perform(get("/parts/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "user")
    @Test
    public void whenGetAllPartStockOrders_ReturnList() throws Exception {
        List<StockOrderDTO> stockOrderDTOList = new ArrayList<>();

        stockOrderDTOList.add(new StockOrderDTO("Cardiff", "supplierOne@test.com", 2000.00, Timestamp.valueOf("2022-01-29 11:17:43"), "Wing A", 20));
        stockOrderDTOList.add(new StockOrderDTO("St Athen", "supplierTwo@test.com", 3000.00, Timestamp.valueOf("2022-01-29 11:17:43"), "Wing A", 20));

        when(stockControlService.getAllPreviousStockOrders()).thenReturn(stockOrderDTOList);

        MvcResult mvcResult = mockMvc.perform(get("/parts/stock-order/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].locationName").value("Cardiff"))
                .andExpect(jsonPath("$[0].supplierEmail").value("supplierOne@test.com"))
                .andExpect(jsonPath("$[1].locationName").value("St Athen"))
                .andExpect(jsonPath("$[1].supplierEmail").value("supplierTwo@test.com"))
                .andReturn();
    }

}


