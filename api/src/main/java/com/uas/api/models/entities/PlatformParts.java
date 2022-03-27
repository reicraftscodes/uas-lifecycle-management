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
    @JoinColumn(name = "PlatformID", referencedColumnName = "PlatformID")
    private Platform platform;

    /**
     * PartID.
     */
    @OneToOne
    @JoinColumn(name = "PartID", referencedColumnName = "PartID")
    private Part part;

}
