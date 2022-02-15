package com.uas.api.models.entities.enums;

public enum PartName {
    WING_A("Wing A"),
    WING_B("Wing B"),
    FUSELAGE("Fuselage"),
    TAIL("Tail"),
    PROPELLER("Propeller"),
    MOTOR("Motor"),
    COMMUNICATIONS_RADIO("Communications Radio"),
    PAYLOAD_ELECTO_OPTICAL("Payload Electo Optical"),
    PAYLOAD_INFRA_RED("Payload Infra-Red"),
    GIMBLE("Gimble"),
    QUAD_ARM("Quad Arm");

    private final String name;

    PartName(final String name) {
        this.name = name;
    }

    /**
     * Getter for name.
     * @return name.
     */
    public String getName() {
        return name;
    }

}
