package com.uas.api.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Locations")
public class Location {
    /**
     * Name of location.
     */
    @Id
    @Column(name = "locationname")
    private String locationName;
    /**
     * Address line 1.
     */
    @Column(name = "addressline1")
    private String addressLine1;
    /**
     * Address Line 2.
     */
    @Column(name = "addressline2")
    private String addressLine2;
    /**
     * Postcode.
     */
    @Column(name = "postcode")
    private String postcode;
    /**
     * Country.
     */
    @Column(name = "country")
    private String country;

}
