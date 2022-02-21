package com.uas.api.models.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Role id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Role name
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     * Role constructor
     */
    public Role() {

    }

    /**
     * Role constructor
     */
    public Role(ERole name) {
        this.name = name;
    }

    /**
     * get user Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * set user Id
     */

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get user's name
     */
    public ERole getName() {
        return name;
    }

    /**
     * set user's name
     */
    public void setName(ERole name) {
        this.name = name;
    }
}
