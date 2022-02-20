package com.uas.api.models.entities.enums;

public class StringToEnumConverter {
    /**
     *  converts a string value into a partStatus enum.
     * @param partStatusValue string part status value.
     * @return returns a partStatus enum.
     * @throws Exception throws an exception if the part status isn't valid.
     */
    public PartStatus stringToPartStatus(final String partStatusValue) throws Exception {

        PartStatus partStatus;
        if (partStatusValue.equals("Operational")) {
            partStatus = PartStatus.OPERATIONAL;
        } else if (partStatusValue.equals("Awaiting Repair")) {
            partStatus = PartStatus.AWAITING_REPAIR;
        } else if (partStatusValue.equals("Being Repaired")) {
            partStatus = PartStatus.BEING_REPAIRED;
        } else if (partStatusValue.equals("Beyond Repair")) {
            partStatus = PartStatus.BEYOND_REPAIR;
        } else {
            throw new Exception("Invalid partStatus.");
        }

        return partStatus;
    }
}
