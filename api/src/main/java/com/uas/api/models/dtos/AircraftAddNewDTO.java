package com.uas.api.models.dtos;

public class AircraftAddNewDTO {
    private final String platformStatus;
    private final String location;
    private final String platformType;
    private final String tailNumber;

    public AircraftAddNewDTO(final String platformStatus, final String location, final String platformType, final String tailNumber) {
        this.platformStatus = platformStatus;
        this.location = location;
        this.platformType = platformType;
        this.tailNumber = tailNumber;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public String getLocation() {
        return location;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getTailNumber() {
        return tailNumber;
    }
}
