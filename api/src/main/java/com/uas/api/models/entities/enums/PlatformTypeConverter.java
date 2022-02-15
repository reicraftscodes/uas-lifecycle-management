package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PlatformTypeConverter implements AttributeConverter<PlatformType, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformTypeConverter.class);
    /**
     * Convert to database column.
     * @param partName the part name to convert.
     * @return the name of the part.
     */
    @Override
    public String convertToDatabaseColumn(final PlatformType partName) {
        if (partName == null) {
            return null;
        }
        return partName.getName();
    }
    /**
     * Convert to entity attribute.
     * @param name the name of part.
     * @return the platform name.
     */
    @Override
    public PlatformType convertToEntityAttribute(final String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PlatformType.values())
                .filter(pn -> pn.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
