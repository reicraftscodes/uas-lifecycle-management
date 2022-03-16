package com.uas.api.models.dtos;

import java.util.List;

public class PlatformStatusAndroidFullDTO {
    /**
     * All aircraft that are operational.
     */
    private final List<PlatformStatusAndroidDTO> operational;
    /**
     * All aircraft that are being repaired.
     */
    private final List<PlatformStatusAndroidDTO> beingRepaired;
    /**
     * All aircraft that are awaiting repair.
     */
    private final List<PlatformStatusAndroidDTO> awaitingRepair;
    /**
     * All aircraft that are beyond repair.
     */
    private final List<PlatformStatusAndroidDTO> beyondRepair;

    /**
     * Constructor.
     * @param operational
     * @param beingRepaired
     * @param awaitingRepair
     * @param beyondRepair
     */
    public PlatformStatusAndroidFullDTO(final List<PlatformStatusAndroidDTO> operational, final List<PlatformStatusAndroidDTO> beingRepaired, final List<PlatformStatusAndroidDTO> awaitingRepair, final List<PlatformStatusAndroidDTO> beyondRepair) {
        this.operational = operational;
        this.beingRepaired = beingRepaired;
        this.awaitingRepair = awaitingRepair;
        this.beyondRepair = beyondRepair;
    }

    /**
     * Gets the operational aircraft.
     * @return the aircraft.
     */
    public List<PlatformStatusAndroidDTO> getOperational() {
        return operational;
    }

    /**
     * Gets the being repaired aircraft.
     * @return the aircraft.
     */
    public List<PlatformStatusAndroidDTO> getBeingRepaired() {
        return beingRepaired;
    }

    /**
     * Gets the awaiting repair aircraft.
     * @return the aircraft.
     */
    public List<PlatformStatusAndroidDTO> getAwaitingRepair() {
        return awaitingRepair;
    }

    /**
     * Gets the beyond repair aircraft.
     * @return the aircraft.
     */
    public List<PlatformStatusAndroidDTO> getBeyondRepair() {
        return beyondRepair;
    }
}
