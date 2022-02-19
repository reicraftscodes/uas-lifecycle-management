package com.uas.api.models.entities.enums;

public class StringToEnumConverter {
    public PartStatus stringToPartStatus(String partStatusValue) throws Exception {

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
            throw new Exception("Not valid partStatus.");
        }

        return partStatus;
    }
}
