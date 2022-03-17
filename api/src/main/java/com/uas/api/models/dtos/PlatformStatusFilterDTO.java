package com.uas.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlatformStatusFilterDTO {

    private final List<String> locations;

    private final List<String> platformStatuses;
}
