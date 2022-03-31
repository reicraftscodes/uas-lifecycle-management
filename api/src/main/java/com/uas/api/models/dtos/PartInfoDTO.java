package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class PartInfoDTO {
    /**
     * The partID of the searched part.
     */
    private final long partID;
    /**
     *  The price of the searched part.
     */
    private final BigDecimal price;
    /**
     * The weight of the searched part.
     */
    private final long weight;
    /**
     * The failure time of the searched part.
     */
    private final long failureTime;
    /**
     * The status of the searched part.
     */
    private final String status;
}
