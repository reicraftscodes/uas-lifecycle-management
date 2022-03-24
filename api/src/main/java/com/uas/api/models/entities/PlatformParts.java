package com.uas.api.models.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PlatformParts")
public class PlatformParts {

    /**
     * PlatformPart id.
     */
    @Id
    @Column(name = "PlatformPartID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * PlatformID.
     */
    @OneToOne
    @Column(name = "PlatformID")
    private Platform platform;

    /**
     * PartID.
     */
    @OneToOne
    @Column(name = "PartID")
    private Part part;

}
