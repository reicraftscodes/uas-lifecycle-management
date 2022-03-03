package com.uas.api.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AircraftTotalRepairsDTO {

    /**
     * List of cumulative repair totals.
     */
    List<Integer> repairTotals;

}
