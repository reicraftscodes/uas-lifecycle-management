package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PlatformType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Platforms")
public class Platform {

    /**
     * PlatformPart id.
     */
    @Id
    @Column(name = "PlatformID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * PlatformType.
     */
    @Column(name = "PlatformType")
    private PlatformType platformType;

}
