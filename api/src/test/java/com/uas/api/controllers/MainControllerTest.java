package com.uas.api.controllers;

import com.uas.api.controller.MainController;
import com.uas.api.models.dtos.LocationStockLevelsDTO;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.requests.MoreStockRequest;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@WebMvcTest(controllers = MainController.class)
public class MainControllerTest {

    @MockBean
    PartService partService;

    @MockBean
    StockControlService stockControlService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenGetPartsAtLowStockReturnPartsDetails() throws Exception {
        List<PartStockLevelDTO> partStockLevelDTOs = new ArrayList<>();
        partStockLevelDTOs.add(new PartStockLevelDTO("Motor", "Cardiff", 10d));
        partStockLevelDTOs.add(new PartStockLevelDTO("Fuselage", "Bristol", 35d));
        when(partService.getPartsAtLowStock()).thenReturn(partStockLevelDTOs);

        mockMvc.perform(get("/api/parts/low-stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].partName").value("Motor"))
                .andExpect(jsonPath("$[0].location").value("Cardiff"))
                .andExpect(jsonPath("$[0].stockLevelPercentage").value(10d))
                .andExpect(jsonPath("$[1].partName").value("Fuselage"))
                .andExpect(jsonPath("$[1].location").value("Bristol"))
                .andExpect(jsonPath("$[1].stockLevelPercentage").value(35d));

        verify(this.partService, times(1)).getPartsAtLowStock();
        verifyNoMoreInteractions(this.partService);
    }

    @Test
    public void whenRequestMoreStockThenShouldBeTrue(){
        MoreStockRequest mockStockRequest = new MoreStockRequest("Cardiff", 100.00, new ArrayList<>(), new ArrayList<>());
        when(stockControlService.addMoreStock(mockStockRequest)).thenReturn(true);
        Assertions.assertTrue(stockControlService.addMoreStock(mockStockRequest));
    }

    @Test
    public void whenGetPartsStockReturnLocationStockLevelsDetails() throws Exception {
        List<LocationStockLevelsDTO> locationStockLevelsDTOs = new ArrayList<>();
        List<PartStockLevelDTO> partStockLevelStAthenDTOs = new ArrayList<>();
        List<PartStockLevelDTO> partStockLevelCardiffDTOs = new ArrayList<>();
        partStockLevelStAthenDTOs.add(new PartStockLevelDTO("Motor", "St Athen", 10d));
        partStockLevelStAthenDTOs.add(new PartStockLevelDTO("Fuselage", "St Athen", 35d));
        partStockLevelCardiffDTOs.add(new PartStockLevelDTO("Motor", "Cardiff", 75d));
        locationStockLevelsDTOs.add(new LocationStockLevelsDTO("St Athen", partStockLevelStAthenDTOs));
        locationStockLevelsDTOs.add(new LocationStockLevelsDTO("Cardiff", partStockLevelCardiffDTOs));


        when(partService.getPartStockLevelsForAllLocations()).thenReturn(locationStockLevelsDTOs);

        mockMvc.perform(get("/api/parts/stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].location").value("St Athen"))
                .andExpect(jsonPath("$[0].partStockLevelDTOs", hasSize(2)))
                .andExpect(jsonPath("$[0].partStockLevelDTOs[0].partName").value("Motor"))
                .andExpect(jsonPath("$[0].partStockLevelDTOs[0].location").value("St Athen"))
                .andExpect(jsonPath("$[0].partStockLevelDTOs[0].stockLevelPercentage").value(10d))
                .andExpect(jsonPath("$[1].location").value("Cardiff"))
                .andExpect(jsonPath("$[1].partStockLevelDTOs", hasSize(1)))
                .andExpect(jsonPath("$[1].partStockLevelDTOs[0].partName").value("Motor"))
                .andExpect(jsonPath("$[1].partStockLevelDTOs[0].location").value("Cardiff"))
                .andExpect(jsonPath("$[1].partStockLevelDTOs[0].stockLevelPercentage").value(75d));

        verify(this.partService, times(1)).getPartStockLevelsForAllLocations();
        verifyNoMoreInteractions(this.partService);
    }

}