package com.uas.api.controller;

import com.uas.api.entities.Aircraft;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {

    @PostMapping(value = "/add", consumes = "application/json")
    void addAircraft(@RequestBody String test){

        //{"tailNumber":"G-101","location":"London","platformStatus":"Operational","platformType":"Platform A"}
        System.out.println(test);
    }
}
