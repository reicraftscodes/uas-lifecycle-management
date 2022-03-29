package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePartWeightDTO {
    /**
     * The partID of the part being updated.
     */
    long partID;
    /**
     * The new weight being set.
     */
    long weight;
}
