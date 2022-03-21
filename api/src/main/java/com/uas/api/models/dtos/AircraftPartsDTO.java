package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AircraftPartsDTO {
    /**
     * The aircraft status.
     */
    private String status;
    /**
     * The parts with their part number, part type, and part status.
     */
    private List<List<String>> parts;
}
