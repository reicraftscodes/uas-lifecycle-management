package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePartFailureTimeDTO {
    /**
     * The partID of the part being updated.
     */
    long partID;
    /**
     * The new failure time value.
     */
    long failureTime;
}
