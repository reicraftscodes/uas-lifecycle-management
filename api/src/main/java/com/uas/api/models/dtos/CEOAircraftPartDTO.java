package com.uas.api.models.dtos;

import com.uas.api.models.entities.Repair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CEOAircraftPartDTO {
    private String partName;
    private double partCost;
    private String partStatus;
    private List<CEOPartRepairDTO> repairs;
}
