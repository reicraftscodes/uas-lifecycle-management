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

    /**
     * Sets title required for testing.
     * @param title the title to set.
     */
    void setPartType(String title);

    /**
     * Sets the failure time required for testing.
     * @param failureTime the failure time to set.
     */
    void setTypicalFailureTime(Long failureTime);
}
