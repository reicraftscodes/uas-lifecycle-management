package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePartPriceDTO {
    /**
     * The partID of the part being updated.
     */
    private long partID;
    /**
     * The new price of the part.
     */
    private double price;
}
