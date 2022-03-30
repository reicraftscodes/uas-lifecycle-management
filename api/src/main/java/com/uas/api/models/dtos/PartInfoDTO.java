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
    long partID;
    /**
     *  The price of the searched part.
     */
    BigDecimal price;
    /**
     * The weight of the searched part.
     */
    long weight;
    /**
     * The failure time of the searched part.
     */
    long failureTime;
    /**
     * The status of the searched part.
     */
    String status;
}
