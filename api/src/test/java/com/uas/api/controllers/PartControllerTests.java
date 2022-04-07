package com.uas.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.PartsController;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.Location;
import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.Stock;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.StockRepository;
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

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
    private LocationRepository locationRepository;

    @MockBean
    private StockRepository stockRepository;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void transferPartNoLocation() throws Exception {
        String msg = "Failure, one or both locations do not exist in the database.";

        when(partService.transferPart("London","Cardiff", "Boeing Wing A", 1)).thenReturn(msg);

        MvcResult mvcRes = mockMvc.perform(get("/parts/transfer/London/Cardiff/Boeing Wing A/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Failure, one or both locations do not exist in the database.\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void transferPartNoStock() throws Exception {
        String msg = "Failure, no stock to transfer.";
        Location location = new Location();
        location.setLocationName("London");
        locationRepository.save(location);

        Location location1 = new Location();
        location1.setLocationName("Cardiff");
        locationRepository.save(location1);

        when(partService.transferPart("London","Cardiff", "Boeing Wing A", 1)).thenReturn(msg);

        MvcResult mvcRes = mockMvc.perform(get("/parts/transfer/London/Cardiff/Boeing Wing A/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Failure, no stock to transfer.\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void transferPartsWithStock() throws Exception {
        String msg = "Success.";

        Part part = new Part(1L, "Boeing Wing A");

        Location location = new Location();
        location.setLocationName("London");
        locationRepository.save(location);

        Location location1 = new Location();
        location1.setLocationName("Cardiff");
        locationRepository.save(location1);

        Stock stock = new Stock(part, 6L, location);
        Stock stock1 = new Stock(part, 0L, location1);
        stockRepository.save(stock);
        stockRepository.save(stock1);

        when(partService.transferPart("London", "Cardiff","Boeing Wing A", 4)).thenReturn(msg);

        MvcResult mvcRes = mockMvc.perform(get("/parts/transfer/London/Cardiff/Boeing Wing A/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success.\"}", mvcRes.getResponse().getContentAsString());

    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void deletePartsNoStock() throws Exception {
        String msg = "Failure, no stock to delete.";
        when(partService.deletePart("London", "Boeing Wing A", 4)).thenReturn(msg);

        MvcResult mvcRes = mockMvc.perform(get("/parts/delete/London/Boeing Wing A/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Failure, no stock to delete.\"}", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void deletePartsWithStock() throws Exception {
        String msg = "Success.";
        Part part = new Part(1L, "Boeing Wing A");
        Location location = new Location();
        location.setLocationName("London");
        locationRepository.save(location);
        Stock stock = new Stock(part, 6L, location);
        stockRepository.save(stock);

        when(partService.deletePart("London", "Boeing Wing A", 4)).thenReturn(msg);

        MvcResult mvcRes = mockMvc.perform(get("/parts/delete/London/Boeing Wing A/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success.\"}", mvcRes.getResponse().getContentAsString());
    }

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

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartStatusSuccess() throws Exception {
        UpdatePartStatusDTO updatePartStatusDTO = new UpdatePartStatusDTO(1l,"OPERATIONAL");
        String json = objectMapper.writeValueAsString(updatePartStatusDTO);

        mockMvc.perform(post("/parts/update-part-status")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartStatusInvalidPart() throws Exception {
        UpdatePartStatusDTO updatePartStatusDTO = new UpdatePartStatusDTO(1l,"OPERATIONAL");
        String json = objectMapper.writeValueAsString(updatePartStatusDTO);
        doThrow(NotFoundException.class).when(partService).updatePartStatus(anyLong(), anyString());
        mockMvc.perform(post("/parts/update-part-status")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartPriceSuccess() throws Exception {
        UpdatePartPriceDTO dto = new UpdatePartPriceDTO(1l,1000.0);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/parts/update-part-price")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartPriceInvalidPart() throws Exception {
        UpdatePartPriceDTO dto = new UpdatePartPriceDTO(1l,1000.0);
        String json = objectMapper.writeValueAsString(dto);
        doThrow(NotFoundException.class).when(partService).updatePartPrice(anyLong(), anyDouble());
        mockMvc.perform(post("/parts/update-part-price")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());

    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartWeightSuccess() throws Exception {
        UpdatePartWeightDTO dto = new UpdatePartWeightDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/parts/update-part-weight")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartWeightInvalidPart() throws Exception {
        UpdatePartWeightDTO dto = new UpdatePartWeightDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);
        doThrow(NotFoundException.class).when(partService).updatePartWeight(anyLong(), anyLong());
        mockMvc.perform(post("/parts/update-part-weight")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartFailureTimeSuccess() throws Exception {
        UpdatePartFailureTimeDTO dto = new UpdatePartFailureTimeDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/parts/update-part-failure-time")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void setPartFailureTimeInvalidPart() throws Exception {
        UpdatePartFailureTimeDTO dto = new UpdatePartFailureTimeDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);
        doThrow(NotFoundException.class).when(partService).updateFailureTime(anyLong(), anyLong());
        mockMvc.perform(post("/parts/update-part-failure-time")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(roles = "USER_LOGISTIC")
    @Test
    public void getPartInfo() throws Exception {
        PartInfoDTO partInfoDTO = new PartInfoDTO(1l, BigDecimal.valueOf(100), 1000l, 500l, "Operational");

        when(partService.getPartInfo(1)).thenReturn(partInfoDTO);
        mockMvc.perform(post("/parts/get-part")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partID").value(1))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.weight").value(1000))
                .andExpect(jsonPath("$.failureTime").value(500))
                .andExpect(jsonPath("$.status").value("Operational"));

        //{"partID":1,"price":100,"weight":1000,"failureTime":500,"status":"Operational"}

    }
    @WithMockUser(roles = "USER_LOGISTIC")
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


