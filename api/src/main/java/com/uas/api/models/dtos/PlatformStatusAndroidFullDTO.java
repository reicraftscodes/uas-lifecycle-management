package com.uas.api.models.dtos;

import java.util.List;

public class PlatformStatusAndroidFullDTO {
    private final List<PlatformStatusAndroidDTO> operational;
    private final List<PlatformStatusAndroidDTO> beingRepaired;
    private final List<PlatformStatusAndroidDTO> awaitingRepair;
    private final List<PlatformStatusAndroidDTO> beyondRepair;

    public PlatformStatusAndroidFullDTO(final List<PlatformStatusAndroidDTO> operational, final List<PlatformStatusAndroidDTO> beingRepaired, final List<PlatformStatusAndroidDTO> awaitingRepair, final List<PlatformStatusAndroidDTO> beyondRepair) {
        this.operational = operational;
        this.beingRepaired = beingRepaired;
        this.awaitingRepair = awaitingRepair;
        this.beyondRepair = beyondRepair;
    }

    public List<PlatformStatusAndroidDTO> getOperational() {
        return operational;
    }

    public List<PlatformStatusAndroidDTO> getBeingRepaired() {
        return beingRepaired;
    }

    public List<PlatformStatusAndroidDTO> getAwaitingRepair() {
        return awaitingRepair;
    }

    public List<PlatformStatusAndroidDTO> getBeyondRepair() {
        return beyondRepair;
    }
}
