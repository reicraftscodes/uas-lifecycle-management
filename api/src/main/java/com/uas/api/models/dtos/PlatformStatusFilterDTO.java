package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlatformStatusFilterDTO {
    /**
     * The locations to be included in the search.
     */
    private final List<String> locations;
    /**
     * The platform statuses to be included in the search.
     */
    private final List<String> platformStatuses;
}
