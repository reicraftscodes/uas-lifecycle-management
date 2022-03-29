package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PartStockDTO {

    private final long partNumber;

    private final String location;

    private final long stockCount;
}
