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

    @Override
    public String convertToDatabaseColumn(PlatformType partName) {
        if (partName == null) {
            return null;
        }
        return partName.getName();
    }

    @Override
    public PlatformType convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PlatformType.values())
                .filter(pn -> pn.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
