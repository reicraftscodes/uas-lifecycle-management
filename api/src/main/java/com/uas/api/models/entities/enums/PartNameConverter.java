package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

// Ref: https://www.baeldung.com/jpa-persisting-enums-in-jpa#converter
@Converter(autoApply = true)
public class PartNameConverter implements AttributeConverter<PartName, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartNameConverter.class);

    /**
     * Convert to database column.
     * @param partName the part name to convert.
     * @return the name of the part.
     */
    @Override
    public String convertToDatabaseColumn(final PartName partName) {
        if (partName == null) {
            return null;
        }
        return partName.getName();
    }

    /**
     * Convert to entity attribute.
     * @param name the name of part.
     * @return the part name.
     */
    @Override
    public PartName convertToEntityAttribute(final String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PartName.values())
                .filter(pn -> pn.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
