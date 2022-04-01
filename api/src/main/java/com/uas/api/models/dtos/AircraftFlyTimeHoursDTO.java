package com.uas.api.models.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AircraftFlyTimeHoursDTO {

    /**
     * The hours operational.
     */
    private List<Integer> flyTimeHours;

}
