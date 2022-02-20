package com.uas.api.controllers;

import com.uas.api.services.PartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PartControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private PartService partService;



    @Test
    public void createPartWithAllParams() throws Exception {

        String exampleJSON = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"Operational\"}";

        HashMap<String,String> requestData = new HashMap<String, String>();
        requestData.put("partType","1");
        requestData.put("aircraft","G-001");
        requestData.put("location","London");
        requestData.put("manufacture","2022-02-20 11:00:00");
        requestData.put("partStatus","Operational");

        String json = "{\"partType\":\"1\",\"aircraft\":\"G-001\",\"location\":\"London\",\"manufacture\":\"2022-02-20 11:00:00\",\"partStatus\":\"Operational\"}";

        //mockMvc.perform(post("/parts/add").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect()




//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post("/parts/add")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(exampleJSON)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
//
//        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
//
//        assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());
    }

}
