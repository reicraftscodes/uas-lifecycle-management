package com.uas.api.repositories.projections;

import com.blazebit.persistence.view.EntityView;
import com.uas.api.models.entities.PartType;
import org.springframework.beans.factory.annotation.Value;
@EntityView(PartType.class)
public interface PartTypeFailureTimeProjection {
    PartType getPartName();
    Long getTypicalFailureTime();
}
