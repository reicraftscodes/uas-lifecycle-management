package com.uas.api.controllers;

import com.uas.api.controller.MainController;
import com.uas.api.models.dtos.PartStockLevelDTO;
import com.uas.api.services.PartService;
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
}
