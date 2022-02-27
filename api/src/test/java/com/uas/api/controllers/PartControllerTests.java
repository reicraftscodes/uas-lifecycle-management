package com.uas.api.controllers;

import com.uas.api.controller.PartsController;
import com.uas.api.security.jwt.AuthEntryPointJwt;
import com.uas.api.security.jwt.JwtUtils;
import com.uas.api.services.PartService;
import com.uas.api.services.StockControlService;
import com.uas.api.services.auth.UserDetailsServiceImpl;
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

import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PartsController.class)
public class PartControllerTests {

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

    @MockBean
    private PartsController partsController;

    @Test
    void contentLoads() {
        assertThat(partsController).isNotNull();
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithAllParams() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType", "1");
        data.put("aircraft", "G-001");
        data.put("location", "London");
        data.put("manufacture", "2022-02-20 11:00:00");
        data.put("partStatus", "OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithAircraft() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType", "1");
        data.put("aircraft", "G-001");
        data.put("location", "London");
        data.put("manufacture", "");
        data.put("partStatus", "OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse)
                .andExpect(status()
                        .isOk()).andReturn();

        assertEquals("", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithManufacture() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType", "1");
        data.put("aircraft", "");
        data.put("location", "London");
        data.put("manufacture", "2022-02-20 11:00:00");
        data.put("partStatus", "OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("", mvcRes.getResponse().getContentAsString());
    }

    @WithMockUser(value = "user")
    @Test
    public void createPartWithNeither() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"\",\"location\":\"London\",\"manufacture\":\"\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType", "1");
        data.put("aircraft", "");
        data.put("location", "London");
        data.put("manufacture", "");
        data.put("partStatus", "OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("", mvcRes.getResponse().getContentAsString());
    }

}


