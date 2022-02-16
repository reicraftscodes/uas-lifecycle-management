package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class LocationStockLevelsDTO {

    /**
     * The location name.
     */
    private String location;

    /**
     * List of part stock levels.
     */
    private List<PartStockLevelDTO> partStockLevelDTOs;
}
