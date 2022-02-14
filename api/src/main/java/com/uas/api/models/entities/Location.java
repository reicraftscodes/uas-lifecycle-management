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
@Table(name="Locations")
public class Location {

    @Id
    @Column(name="LocationName")
    private String locationName;

    @Column(name="AddressLine1")
    private String addressLine1;

    @Column(name="AddressLine2")
    private String addressLine2;

    @Column(name="PostCode")
    private String postcode;

    @Column(name="Country")
    private String country;

}
