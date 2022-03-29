package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePartPriceDTO {
    long partID;
    double price;
}
