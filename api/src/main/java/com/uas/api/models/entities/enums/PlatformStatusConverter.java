package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

// Ref: https://www.baeldung.com/jpa-persisting-enums-in-jpa#converter
@Converter(autoApply = true)
public class PlatformStatusConverter implements AttributeConverter<PlatformStatus, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformStatusConverter.class);
    /**
     * Convert to database column.
     * @param platformStatus the platform status to convert.
     * @return the label of the platform status.
     */
    @Override
    public String convertToDatabaseColumn(final PlatformStatus platformStatus) {
        if (platformStatus == null) {
            return null;
        }
        return platformStatus.getLabel();
    }
    /**
     * Convert to entity attribute.
     * @param label the platform status label.
     * @return the converted platform status.
     */
    @Override
    public PlatformStatus convertToEntityAttribute(final String label) {
        if (label == null) {
            return null;
        }

        return Stream.of(PlatformStatus.values())
                .filter(ps -> ps.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
