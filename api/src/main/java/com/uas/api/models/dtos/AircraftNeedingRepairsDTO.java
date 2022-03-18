package com.uas.api.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AircraftNeedingRepairsDTO {

    /**
     * The number of Aircraft that have parts requiring repairs.
     */
    private final Integer aircraftNeedingRepair;

}
