package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartTypeFailureTimeDTO {
    /**
     * The part type.
     */
    private final String partType;
    /**
     * The failure time.
     */
    private final long failureTime;
}
