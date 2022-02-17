package com.uas.api.controller;

import com.uas.api.models.entities.Part;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parts")
public class PartsController {

    @PostMapping(value="/add", consumes = "application/json")
    ResponseEntity<?> addPart(@RequestBody Part part) {

        
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
