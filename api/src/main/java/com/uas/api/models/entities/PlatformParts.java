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
     * parttypeid.
     */
    @OneToOne
    @JoinColumn(name = "parttypeid", referencedColumnName = "parttypeid")
    private PartType part;

}
