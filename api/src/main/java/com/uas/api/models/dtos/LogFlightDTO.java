package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LogFlightDTO {
    /**
     * aircraft from the user input.
     */
    private String aircraft;

    /**
     * flytime from the user input.
     */
    private int flyTime;
}
