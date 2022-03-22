package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartName;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "PartTypes")
public class PartType {
    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PartID")
    private Long id;
    /**
     * Part name.
     */
    @Column(name = "PartType")
    private PartName partName;


}
