package com.uas.api.models.dtos;

import lombok.Getter;

@Getter
public class AddPartDTO {
    /**
     * The partType id.
     */
    private final Long partType;
    /**
     * The part name.
     */
    private final String partName;
    /**
     * The location name.
     */
    private final String locationName;
    /**
     * The manufacture date as a string.
     */
    private final String manufacture;
    /**
     * The price of part.
     */
    private final double price;
    /**
     * The weight of part.
     */
    private final long weight;
    /**
     * The aircraft part is assigned to.
     */
    private final String aircraft;
    /**
     * The status of the part.
     */
    private final String partStatus;


    /**
     * Constructor for a AddPartDTO.
     * @param partType Type of part.
     * @param price Price of the part.
     * @param weight Weight of the part.
     * @param partName Name of the part.
     * @param manufacture Timestamp of part.
     * @param aircraft Aircraft part assigned to.
     * @param locationName Location part assigned to.
     * @param partStatus Status of the part.
     */
    public AddPartDTO(final Long partType, final String partName, final String locationName, final String manufacture, final double price, final long weight, final String aircraft, final String partStatus) {
        this.partType = partType;
        this.partName = partName;
        this.locationName = locationName;
        this.manufacture = manufacture;
        this.price = price;
        this.weight = weight;
        this.aircraft = aircraft;
        this.partStatus = partStatus;
    }
}
