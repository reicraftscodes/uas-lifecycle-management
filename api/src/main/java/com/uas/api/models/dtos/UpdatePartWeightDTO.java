package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePartWeightDTO {
    /**
     * The partID of the part being updated.
     */
    private long partID;
    /**
     * The new weight being set.
     */
    private long weight;
}
