package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAircraftPartDTO {
    /**
     * The part number of the part being replaced.
     */
    private long currentPartNumber;
    /**
     * The part number of the new part.
     */
    private long newPartNumber;
}
