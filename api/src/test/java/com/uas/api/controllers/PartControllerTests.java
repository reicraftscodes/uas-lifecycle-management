package com.uas.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uas.api.controller.PartsController;
import com.uas.api.models.dtos.*;
import com.uas.api.models.entities.enums.PlatformStatus;
import com.uas.api.models.entities.enums.PlatformType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @WithMockUser(value = "user")
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

    @WithMockUser(value = "user")
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

    @WithMockUser(value = "user")
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
        MoreStockRequest newStock = new MoreStockRequest("Cardiff", "sncmsuktestemail@gmail.com", partTypes, quantities);
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

    @WithMockUser(value = "user")
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
    public void setPartStatusSuccess() throws Exception {
        UpdatePartStatusDTO updatePartStatusDTO = new UpdatePartStatusDTO(1l,"OPERATIONAL");
        String json = objectMapper.writeValueAsString(updatePartStatusDTO);

        when(partService.updatePartStatus(updatePartStatusDTO.getPartID(),updatePartStatusDTO.getPartStatus())).thenReturn("Success");
        mockMvc.perform(post("/parts/update-part-status")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartStatusInvalidPart() throws Exception {
        UpdatePartStatusDTO updatePartStatusDTO = new UpdatePartStatusDTO(1l,"OPERATIONAL");
        String json = objectMapper.writeValueAsString(updatePartStatusDTO);

        when(partService.updatePartStatus(updatePartStatusDTO.getPartID(),updatePartStatusDTO.getPartStatus())).thenReturn("Part not found!");
            mockMvc.perform(post("/parts/update-part-status")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartPriceSuccess() throws Exception {
        UpdatePartPriceDTO dto = new UpdatePartPriceDTO(1l,1000.0);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updatePartPrice(dto.getPartID(),dto.getPrice())).thenReturn("Success");
        mockMvc.perform(post("/parts/update-part-price")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartPriceInvalidPart() throws Exception {
        UpdatePartPriceDTO dto = new UpdatePartPriceDTO(1l,1000.0);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updatePartPrice(dto.getPartID(),dto.getPrice())).thenReturn("Part not found!");
        mockMvc.perform(post("/parts/update-part-price")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }

    @WithMockUser(value = "user")
    @Test
    public void setPartWeightSuccess() throws Exception {
        UpdatePartWeightDTO dto = new UpdatePartWeightDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updatePartWeight(dto.getPartID(),dto.getWeight())).thenReturn("Success");
        mockMvc.perform(post("/parts/update-part-weight")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartWeightInvalidPart() throws Exception {
        UpdatePartWeightDTO dto = new UpdatePartWeightDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updatePartWeight(dto.getPartID(),dto.getWeight())).thenReturn("Part not found!");
        mockMvc.perform(post("/parts/update-part-weight")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartFailureTimeSuccess() throws Exception {
        UpdatePartFailureTimeDTO dto = new UpdatePartFailureTimeDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updateFailureTime(dto.getPartID(),dto.getFailureTime())).thenReturn("Success");
        mockMvc.perform(post("/parts/update-part-failure-time")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void setPartFailureTimeInvalidPart() throws Exception {
        UpdatePartFailureTimeDTO dto = new UpdatePartFailureTimeDTO(1l,1000);
        String json = objectMapper.writeValueAsString(dto);

        when(partService.updateFailureTime(dto.getPartID(),dto.getFailureTime())).thenReturn("Part not found!");
        mockMvc.perform(post("/parts/update-part-failure-time")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "user")
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
}


