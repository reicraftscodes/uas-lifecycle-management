package com.uas.api.controller;

import com.uas.api.repositories.AircraftRepository;
import com.uas.api.repositories.LocationRepository;
import com.uas.api.repositories.PartRepository;
import com.uas.api.repositories.PartTypeRepository;
import com.uas.api.services.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/parts")
public class PartsController {

    private final PartService partService;

    @Autowired
    public PartsController(PartService partService) {
        this.partService = partService;
    }

    @PostMapping(value="/add", consumes = "application/json")
    ResponseEntity<?> addPart(@RequestBody HashMap<String,String> requestData) {

        String response = partService.addPartFromJSON(requestData);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
