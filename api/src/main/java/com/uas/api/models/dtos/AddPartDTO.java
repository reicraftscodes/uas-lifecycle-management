package com.uas.api.models.dtos;

import lombok.Getter;

@Getter
public class AddPartDTO {
    /**
     * The partType id.
     */
    private final Long partType;
    /**
     * The part name
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
    private final double price;
    private final long weight;
    private final String aircraft;
    private final String partStatus;

//    //DTO without aircraft but with manufacture
//    public AddPartDTO(Long partType, String partName, String locationName, String manufacture, double price, long weight) {
//        this.partType = partType;
//        this.partName = partName;
//        this.locationName = locationName;
//        this.manufacture = manufacture;
//        this.price = price;
//        this.weight = weight;
//        this.aircraft = null;
//        this.partStatus = null;
//    }
//    //DTO without aircraft and manufacture
//
//    public AddPartDTO(Long partType, String partName, String locationName, double price, long weight) {
//        this.partType = partType;
//        this.partName = partName;
//        this.locationName = locationName;
//        this.price = price;
//        this.weight = weight;
//        this.manufacture = null;
//        this.aircraft = null;
//        this.partStatus = null;
//    }
//
//    //DTO with aircraft but no manufacture
//
//    public AddPartDTO(Long partType, String partName, String locationName, double price, long weight, String aircraft, String partStatus) {
//        this.partType = partType;
//        this.partName = partName;
//        this.locationName = locationName;
//        this.price = price;
//        this.weight = weight;
//        this.aircraft = aircraft;
//        this.partStatus = partStatus;
//        this.manufacture = null;
//    }
//
//
//    //DTO with aircraft and with manufacture


    public AddPartDTO(Long partType, String partName, String locationName, String manufacture, double price, long weight, String aircraft, String partStatus) {
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
