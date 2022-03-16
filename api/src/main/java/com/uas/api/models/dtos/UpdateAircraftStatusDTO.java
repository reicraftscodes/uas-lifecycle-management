package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAircraftStatusDTO {
    /**
     * The tailnumber of the aircraft that is having its status changed.
     */
    private String tailNumber;
    /**
     * The status that the aircraft is being set to.
     */
    private String status;
}
