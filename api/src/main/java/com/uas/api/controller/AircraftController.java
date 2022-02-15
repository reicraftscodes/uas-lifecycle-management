package com.uas.api.controller;


import com.uas.api.models.entities.Aircraft;
import com.uas.api.models.entities.enums.PlatformStatus;
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
        //takes data as hashmap to manually create aircraft as couldn't automatically create it from the json
        // as enums weren't created from the json strings.

        boolean error = false;

        PlatformStatus platformStatus = PlatformStatus.DESIGN;
        switch (requestData.get("platformStatus")) {
            case "Design" : break;
            case "Production" : platformStatus = PlatformStatus.PRODUCTION; break;
            case "Operation" : platformStatus = PlatformStatus.OPERATION; break;
            case "Repair" : platformStatus = PlatformStatus.REPAIR; break;
            default: error = true; break;
        }

        





        if(!error) {
            Aircraft aircraft = new Aircraft(requestData.get("tailNumber"), requestData.get("location"), platformStatus, requestData.get("platformType"));
        }



    }
}
