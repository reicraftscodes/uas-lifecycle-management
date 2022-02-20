package com.uas.api.controllers;

import com.uas.api.controller.MainController;
import com.uas.api.controller.PartsController;
import com.uas.api.services.PartService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = PartsController.class)
public class PartControllerTests {

    @MockBean
    private PartService partService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PartsController partsController;

    @Test
    void contentLoads() {
        assertThat(partsController).isNotNull();
    }


    @Test
    public void createPartWithAllParams() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType","1");
        data.put("aircraft","G-001");
        data.put("location","London");
        data.put("manufacture","2022-02-20 11:00:00");
        data.put("partStatus","OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success\"}",mvcRes.getResponse().getContentAsString());
    }

    @Test
    public void createPartWithAircraft() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType","1");
        data.put("aircraft","G-001");
        data.put("location","London");
        data.put("manufacture","");
        data.put("partStatus","OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success\"}",mvcRes.getResponse().getContentAsString());
    }

    @Test
    public void createPartWithManufacture() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType","1");
        data.put("aircraft","");
        data.put("location","London");
        data.put("manufacture","2022-02-20 11:00:00");
        data.put("partStatus","OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success\"}",mvcRes.getResponse().getContentAsString());
    }

    @Test
    public void createPartWithNeither() throws Exception {
        String json = "{\"partType\":\"1\",\"aircraft\":\"\",\"location\":\"London\",\"manufacture\":\"\",\"partStatus\":\"OPERATIONAL\"}";

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("partType","1");
        data.put("aircraft","");
        data.put("location","London");
        data.put("manufacture","");
        data.put("partStatus","OPERATIONAL");

        Mockito.when(partService.addPartFromJSON(data)).thenReturn("");

        MockHttpServletRequestBuilder mockResponse = MockMvcRequestBuilders.post("/parts/add").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        MvcResult mvcRes = mockMvc.perform(mockResponse).andExpect(status().isOk()).andReturn();

        assertEquals("{\"response\":\"Success\"}",mvcRes.getResponse().getContentAsString());
    }

}
