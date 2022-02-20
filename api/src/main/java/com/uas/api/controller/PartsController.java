package com.uas.api.controller;

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
    /**
     * Communicated with the db about the parts table.
     */
    private final PartService partService;

    /**
     *  Constructor.
     * @param partService Required Service.
     */
    @Autowired
    public PartsController(final PartService partService) {
        this.partService = partService;
    }

    /**
     *  Post request for /parts/add which adds a part from the json.
     * @param requestData The json from the request turned into a hashmap.
     * @return returns a response entity of success or an error with the error message.
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    ResponseEntity<?> addPart(@RequestBody final HashMap<String, String> requestData) {
        String response = partService.addPartFromJSON(requestData);

        if (response.equals("")) {
            return ResponseEntity.ok("{\"response\":\"Success\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"response\":\""+response+"\"}");
        }
    }
}
