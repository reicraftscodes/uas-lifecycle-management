package com.uas.api.repositories.projections;

public interface PartFailureTimeProjection {
    /**
     * Gets the part name.
     * @return the part name.
     */
    String getPartTypeName();

    /**
     * Gets the failure time.
     * @return the failure time in hours as a long.
     */
    Long getTypicalFailureTime();

    /**
     * Sets title required for testing.
     * @param title the title to set.
     */
    void setPartTypeName(String title);

    /**
     * Sets the failure time required for testing.
     * @param failureTime the failure time to set.
     */
    void setTypicalFailureTime(Long failureTime);
}
