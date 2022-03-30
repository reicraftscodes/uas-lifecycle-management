package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartInfoDTO {
    /**
     * The partID of the searched part.
     */
    private long partID;
    /**
     *  The price of the searched part.
     */
    private BigDecimal price;
    /**
     * The weight of the searched part.
     */
    private long weight;
    /**
     * The failure time of the searched part.
     */
    private long failureTime;
    /**
     * The status of the searched part.
     */
    private String status;
}
