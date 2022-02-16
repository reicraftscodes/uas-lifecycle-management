package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

// Ref: https://www.baeldung.com/jpa-persisting-enums-in-jpa#converter
@Converter(autoApply = true)
public class PlatformTypeConverter implements AttributeConverter<PlatformType, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformTypeConverter.class);
    /**
     * Convert to database column.
     * @param partType the platform type to convert.
     * @return the name of the platform type.
     */
    @Override
    public String convertToDatabaseColumn(final PlatformType partType) {
        if (partType == null) {
            return null;
        }
        return partType.getName();
    }
    /**
     * Convert to entity attribute.
     * @param name the name of platform type.
     * @return the platform name.
     */
    @Override
    public PlatformType convertToEntityAttribute(final String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PlatformType.values())
                .filter(pt -> pt.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
