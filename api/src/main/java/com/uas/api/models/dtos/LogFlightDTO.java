package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LogFlightDTO {
    /**
     * User Id.
     */
    private final long userId;
    /**
     * aircraft from the user input.
     */
    private final String aircraft;

    /**
     * flytime from the user input.
     */
    private final int flyTime;
}
