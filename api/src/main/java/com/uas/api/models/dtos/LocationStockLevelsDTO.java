package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class LocationStockLevelsDTO {
    private String location;
    private List<PartStockLevelDTO> partStockLevelDTOs;
}
