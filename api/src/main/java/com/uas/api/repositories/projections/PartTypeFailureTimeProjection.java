package com.uas.api.repositories.projections;

public interface PartTypeFailureTimeProjection {
    /**
     * Gets the part name.
     * @return the part name.
     */
    String getPartType();

    /**
     * Gets the failure time.
     * @return the failure time in hours as a long.
     */
    Long getTypicalFailureTime();
}
