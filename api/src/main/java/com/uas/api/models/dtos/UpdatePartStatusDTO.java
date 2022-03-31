package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdatePartStatusDTO {
    /**
     * The partID of the part being updated.
     */
    private long partID;
    /**
     * The status that the part is being updated to.
     */
    private String partStatus;
}
