package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

// Ref: https://www.baeldung.com/jpa-persisting-enums-in-jpa#converter
@Converter(autoApply = true)
public class PartStatusConverter implements AttributeConverter<PartStatus, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartStatusConverter.class);

    /**
     * Convert to database column.
     * @param partStatus the part status to convert.
     * @return the label of the part status.
     */
    @Override
    public String convertToDatabaseColumn(final PartStatus partStatus) {
        if (partStatus == null) {
            return null;
        }
        return partStatus.getLabel();
    }

    /**
     * Convert to entity attribute.
     * @param label the label of part status.
     * @return the part status.
     */
    @Override
    public PartStatus convertToEntityAttribute(final String label) {
        if (label == null) {
            return null;
        }

        return Stream.of(PartStatus.values())
                .filter(ps -> ps.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
