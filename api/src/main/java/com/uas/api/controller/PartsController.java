package com.uas.api.controller;

import com.uas.api.models.entities.Part;
import com.uas.api.models.entities.PartType;
import com.uas.api.repositories.PartRepository;
import com.uas.api.repositories.PartTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/parts")
public class PartsController {

    private final PartRepository partRepository;
    private final PartTypeRepository partTypeRepository;

    @Autowired
    public PartsController(PartRepository partRepository, PartTypeRepository partTypeRepository) {
        this.partRepository = partRepository;
        this.partTypeRepository = partTypeRepository;
    }

    @PostMapping(value="/add", consumes = "application/json")
    ResponseEntity<?> addPart(@RequestBody HashMap<String,String> requestData) {

        PartType partType = partTypeRepository.findPartTypeById(Long.parseLong(requestData.get("partType")));

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
