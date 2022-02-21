package com.uas.api.controllers;

import com.uas.api.controller.AircraftController;
import com.uas.api.services.AircraftServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = AircraftController.class)
public class AircraftControllerTest {

    @MockBean
    AircraftServiceImpl aircraftService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void AddAircraftWithCorrectJSON() throws Exception {
        String json = "{\"tailNumber\":\"G-999\",\"location\":\"London\",\"platformStatus\":\"DESIGN\",\"platformType\":\"Platform_A\"}";

        mockMvc.perform(post("/aircraft/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json).characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }

}
