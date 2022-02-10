package com.uas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.uas.api.entities.Aircraft;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    @PostMapping(value = "/add", consumes = "application/json")
    void addAircraft(@RequestBody HashMap<String, String> requestData){
        System.out.println(requestData.get("tailNumber"));
        System.out.println(requestData.get("location"));
        System.out.println(requestData.get("platformStatus"));
        System.out.println(requestData.get("platformType"));
        
    }
}
