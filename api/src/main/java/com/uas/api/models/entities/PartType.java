package com.uas.api.models.entities;

import com.uas.api.models.entities.enums.PartName;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "parttypes")
@Table(name = "parttypes")
public class PartType {
    /**
     * Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PartTypeID")
    private Long id;
    /**
     * Part name.
     */
    @Column(name = "PartTypeName")
    private PartName partName;


}
